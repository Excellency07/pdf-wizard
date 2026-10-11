package com.backend.pdf_wizard.model.dto.pdfDtos.fontDtos;

import org.apache.pdfbox.pdmodel.font.PDType0Font;

public record FontSet(
        PDType0Font regular,
        PDType0Font bold,
        PDType0Font italic,
        PDType0Font boldAndItalic
) {
}
