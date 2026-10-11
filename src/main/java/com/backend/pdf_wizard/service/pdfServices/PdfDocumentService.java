package com.backend.pdf_wizard.service.pdfServices;

import com.backend.pdf_wizard.model.PageResponseDto;
import com.backend.pdf_wizard.model.dto.pdfDtos.GeneratePdfRequestDto;
import com.backend.pdf_wizard.model.dto.pdfDtos.PdfResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;

public interface PdfDocumentService {
    byte[] generate(UUID accountId, GeneratePdfRequestDto request);
    PdfResponseDto getPdfMetaData(UUID accountId, UUID pdfId);
    PageResponseDto<PdfResponseDto> getAllPdfs(UUID accountId, Pageable pageable, String keyword, Long minSize, Long maxSize, Instant dateFrom, Instant dateTo);
    PdfResponseDto upload(MultipartFile file, UUID accountId);
    InputStream download(UUID pdfId, UUID accountId);
    void delete(UUID pdfId, UUID accountId);
    void deleteAllPdfs(UUID accountId);
    void sendPdfEmail(UUID accountId, UUID pdfId, String to);
}
