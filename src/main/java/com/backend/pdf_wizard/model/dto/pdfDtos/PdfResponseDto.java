package com.backend.pdf_wizard.model.dto.pdfDtos;

import java.time.Instant;
import java.util.UUID;

public record PdfResponseDto(
        UUID id,
        String filename,
        String storageFilename,
        long fileSize,
        Instant createdAt
) {
}
