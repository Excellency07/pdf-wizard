package com.backend.pdf_wizard.model.dto.pdfDtos;

import com.backend.pdf_wizard.model.enums.PageFormat;
import com.backend.pdf_wizard.model.enums.PdfOrientation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record GeneratePdfRequestDto(
        @NotBlank(message = "Filename is required")
        String filename,

        String title,

        String author,

        String subject,

        PageFormat format,

        @NotNull(message = "Orientation is required")
        PdfOrientation orientation,

        List<PdfElement> elements
) {
}
