package com.backend.pdf_wizard.controller;

import com.backend.pdf_wizard.dao.entity.PdfMetaData;
import com.backend.pdf_wizard.model.PageResponseDto;
import com.backend.pdf_wizard.model.dto.SendPdfEmailRequestDto;
import com.backend.pdf_wizard.model.dto.pdfDtos.GeneratePdfRequestDto;
import com.backend.pdf_wizard.model.dto.pdfDtos.PdfResponseDto;
import com.backend.pdf_wizard.security.AccountDetails;
import com.backend.pdf_wizard.service.pdfServices.PdfDocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/pdf")
@RequiredArgsConstructor
public class PdfController {

    private final PdfDocumentService pdfDocumentService;

    @PostMapping(
            value = "/create",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<byte[]> generate(
            @AuthenticationPrincipal AccountDetails accountDetails,
            @Valid @RequestBody GeneratePdfRequestDto request
    ) {

        byte[] pdf = pdfDocumentService.generate(accountDetails.getId(), request);

        ContentDisposition disposition =
                ContentDisposition.attachment()
                        .filename(request.filename())
                        .build();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        disposition.toString()
                )
                .body(pdf);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponseDto<PdfResponseDto>> searchPdfs(
            @AuthenticationPrincipal AccountDetails accountDetails,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection,
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "5") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long minSize,
            @RequestParam(required = false) Long maxSize,
            @RequestParam(required = false) Instant dateFrom,
            @RequestParam(required = false) Instant dateTo
    ) {
        UUID accountId = accountDetails.getId();

        Sort.Direction direction = (sortDirection.equals("ASC")) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = switch (sortBy) {
            case "filename" -> Sort.by(direction, PdfMetaData::getFilename);
            case "size" -> Sort.by(direction, PdfMetaData::getFileSize);
            case "createdAt" -> Sort.by(direction, PdfMetaData::getCreatedAt);
            default -> Sort.by(direction, PdfMetaData::getId);
        };

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        PageResponseDto<PdfResponseDto> files = pdfDocumentService.getAllPdfs(accountId, pageable, keyword, minSize, maxSize, dateFrom, dateTo);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/{pdfId}")
    public ResponseEntity<PdfResponseDto> getPdfMetaData(
            @AuthenticationPrincipal AccountDetails accountDetails,
            @PathVariable UUID pdfId) {
        UUID accountId = accountDetails.getId();
        PdfResponseDto pdfResponseDto = pdfDocumentService.getPdfMetaData(accountId, pdfId);
        return ResponseEntity.ok(pdfResponseDto);
    }

    @GetMapping("/{pdfId}/download")
    public ResponseEntity<InputStreamResource> downloadFile(
            @AuthenticationPrincipal AccountDetails accountDetails,
            @PathVariable UUID pdfId) {

        UUID accountId = accountDetails.getId();

        PdfResponseDto pdf =
                pdfDocumentService.getPdfMetaData(accountId, pdfId);

        InputStream downloadedFile =
                pdfDocumentService.download(pdfId, accountId);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(pdf.filename())
                                .build()
                                .toString()
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(downloadedFile));
    }

    @PostMapping("/{pdfId}/send-email")
    public ResponseEntity<Void> sendPdfByEmail(
            @AuthenticationPrincipal AccountDetails accountDetails,
            @PathVariable UUID pdfId,
            @Valid @RequestBody SendPdfEmailRequestDto request
    ) {
        UUID accountID = accountDetails.getId();
        pdfDocumentService.sendPdfEmail(
                accountID,
                pdfId,
                request.to()
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{pdfId}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal AccountDetails accountDetails,
                                       @PathVariable UUID pdfId) {
        UUID accountId = accountDetails.getId();
        pdfDocumentService.delete(pdfId, accountId);
        return ResponseEntity.noContent().build();
    }
}
