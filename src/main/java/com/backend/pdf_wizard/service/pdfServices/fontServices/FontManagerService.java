package com.backend.pdf_wizard.service.pdfServices.fontServices;

import com.backend.pdf_wizard.model.dto.pdfDtos.fontDtos.FontSet;
import org.apache.pdfbox.pdmodel.PDDocument;

public interface FontManagerService {
    FontSet loadFonts(PDDocument document);
}
