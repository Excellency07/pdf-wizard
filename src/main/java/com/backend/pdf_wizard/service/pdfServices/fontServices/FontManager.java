package com.backend.pdf_wizard.service.pdfServices.fontServices;

import com.backend.pdf_wizard.model.dto.pdfDtos.fontDtos.FontSet;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class FontManager implements FontManagerService{

    @Override
    public FontSet loadFonts(PDDocument document) {

        PDType0Font regular = load(document, "fonts/DejaVuSans.ttf");
        PDType0Font bold = load(document, "fonts/DejaVuSans-Bold.ttf");
        PDType0Font italic = load(document, "fonts/DejaVuSans-Oblique.ttf");
        PDType0Font boldAndItalic = load(document, "fonts/DejaVuSans-BoldOblique.ttf");

        return new FontSet(regular, bold, italic, boldAndItalic);
    }

    private PDType0Font load(PDDocument document, String path) {
        ClassPathResource pathResource = new ClassPathResource(path);

        try {
            InputStream inputStream = pathResource.getInputStream();
            return PDType0Font.load(document, inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
