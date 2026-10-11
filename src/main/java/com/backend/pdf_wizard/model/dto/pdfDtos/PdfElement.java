package com.backend.pdf_wizard.model.dto.pdfDtos;

import com.backend.pdf_wizard.model.enums.ElementType;
import jakarta.validation.constraints.NotBlank;

public record PdfElement(
        @NotBlank
        ElementType type,

        String content,

        Long fontSize,

        boolean bold,

        boolean italic
) {
}
