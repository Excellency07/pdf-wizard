package com.backend.pdf_wizard.service.pdfServices.renderer;

import com.backend.pdf_wizard.model.dto.pdfDtos.PdfElement;
import com.backend.pdf_wizard.model.dto.pdfDtos.fontDtos.FontSet;
import com.backend.pdf_wizard.service.pdfServices.layout.LayoutContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public interface ElementRenderer {

    public void render(
            PdfElement element,
            LayoutContext context,
            FontSet fontSet
    ) throws IOException;
}
