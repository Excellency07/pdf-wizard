package com.backend.pdf_wizard.service.pdfServices;

import com.backend.pdf_wizard.dao.entity.PdfMetaData;
import com.backend.pdf_wizard.dao.repository.jpa.AccountRepository;
import com.backend.pdf_wizard.dao.repository.jpa.PdfDocumentRepository;
import com.backend.pdf_wizard.exception.ResourceNotFountException;
import com.backend.pdf_wizard.model.PageResponseDto;
import com.backend.pdf_wizard.model.dto.pdfDtos.GeneratePdfRequestDto;
import com.backend.pdf_wizard.model.dto.pdfDtos.PdfResponseDto;
import com.backend.pdf_wizard.model.mapper.PdfMapper;
import com.backend.pdf_wizard.service.emailServices.EmailService;
import com.backend.pdf_wizard.service.storageServices.StorageService;
import com.backend.pdf_wizard.specification.PdfSpecification;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PdfDocumentServiceImpl implements PdfDocumentService{

    private final Tika tika;
    private final StorageService storageService;
    private final PdfGeneratorService pdfGeneratorService;
    private final PdfMapper pdfMapper;
    private final PdfDocumentRepository pdfDocumentRepository;
    private final AccountRepository accountRepository;
    private final EmailService emailService;

    @Transactional
    @Override
    public byte[] generate(UUID accountId, GeneratePdfRequestDto request) {
        byte[] generatedPdf = pdfGeneratorService.generate(request);
        PdfMetaData pdfMetaData = pdfMapper.toEntity(request);
        String storageName = UUID.randomUUID() + ".pdf";

        try (InputStream inputStream = new ByteArrayInputStream(generatedPdf)) {
            storageService.upload(
                    storageName,
                    inputStream,
                    generatedPdf.length,
                    "application/pdf"
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to store generated PDF", e);
        }

        pdfMetaData.setFilename(request.filename());
        pdfMetaData.setAccountId(accountId);
        pdfMetaData.setFileSize(generatedPdf.length);
        pdfMetaData.setStorageName(storageName);
        pdfDocumentRepository.save(pdfMetaData);
        return generatedPdf;
    }

    @Transactional
    @Override
    public PdfResponseDto upload(MultipartFile file, UUID accountId) {
        if (file.isEmpty()) throw new IllegalArgumentException("Empty file is not allowed");

        String type;
        try {
            InputStream inputStream = new BufferedInputStream(file.getInputStream());
            type = tika.detect(inputStream);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        String storageName = UUID.randomUUID() + ".bin";
        try {
            storageService.upload(storageName, file.getInputStream(), file.getSize(), type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String filename = Objects.requireNonNull(file.getOriginalFilename());
        filename = filename.replaceAll("[^A-Za-z0-9.-_]]", "_");

        PdfMetaData fileMetaData = new PdfMetaData(
                filename,
                storageName,
                file.getSize(),
                accountId
        );
        return pdfMapper.toDto(pdfDocumentRepository.save(fileMetaData));
    }

    @Transactional
    @Override
    public InputStream download(UUID pdfId, UUID accountId) {
        PdfMetaData pdfMetaData = pdfDocumentRepository.findByIdAndAccountId(pdfId, accountId)
                .orElseThrow(() -> new ResourceNotFountException("File not found"));

        try {
            return storageService.download(pdfMetaData.getStorageName(), pdfMetaData.getFilename());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public void delete(UUID pdfId, UUID accountId) {
        PdfMetaData pdfMetaData = pdfDocumentRepository.findByIdAndAccountId(pdfId, accountId)
                .orElseThrow(() -> new ResourceNotFountException("File not found"));

        pdfDocumentRepository.delete(pdfMetaData);
        try {
            storageService.delete(pdfMetaData.getStorageName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public void deleteAllPdfs(UUID accountId) {
        List<PdfMetaData> pdfs = pdfDocumentRepository.findByAccountId(accountId);
        for (PdfMetaData pdf : pdfs) {
            pdfDocumentRepository.delete(pdf);
            try {
                storageService.delete(pdf.getStorageName());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void sendPdfEmail(UUID accountId, UUID pdfId, String to) {
        emailService.sendPdf(accountId, pdfId, to);
    }

    @Transactional(readOnly = true)
    public PdfResponseDto getPdfMetaData(UUID accountId, UUID pdfId) {
        PdfMetaData pdfMetaData = pdfDocumentRepository.findByIdAndAccountId(pdfId, accountId)
                .orElseThrow(() -> new ResourceNotFountException("File not found"));
        return pdfMapper.toDto(pdfMetaData);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponseDto<PdfResponseDto> getAllPdfs(UUID accountId, Pageable pageable, String keyword, Long minSize, Long maxSize, Instant dateFrom, Instant dateTo) {
        if (!accountRepository.existsById(accountId)) throw new ResourceNotFountException("Account not found");
        Specification<PdfMetaData> specification = PdfSpecification.getSpecification(accountId, keyword, minSize, maxSize, dateFrom, dateTo);
        Page<PdfMetaData> page = pdfDocumentRepository.findAll(specification, pageable);
        return PageResponseDto.toDto(page.map(pdfMapper::toDto));
    }
}
