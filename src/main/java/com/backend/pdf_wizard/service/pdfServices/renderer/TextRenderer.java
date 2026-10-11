package com.backend.pdf_wizard.service.pdfServices.renderer;

import com.backend.pdf_wizard.model.dto.pdfDtos.PdfElement;
import com.backend.pdf_wizard.model.dto.pdfDtos.fontDtos.FontSet;
import com.backend.pdf_wizard.service.pdfServices.layout.LayoutContext;
import com.backend.pdf_wizard.service.pdfServices.layout.PageManager;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TextRenderer implements ElementRenderer {

    private final PageManager pageManager;

    @Override
    public void render(PdfElement element, LayoutContext context, FontSet fontSet) throws IOException {
        PDType0Font font = resolveFont(element, fontSet);
        float fontSize = element.fontSize();
        float lineHeight = element.fontSize() * 1.4f;

        List<String> lines = wrapText(element.content(), context.getAvailableWidth(), font, fontSize);
        for (String line : lines) {
            pageManager.ensureSpace(context, lineHeight);
            context.getContentStream().beginText();
            context.getContentStream().setFont(font, fontSize);
            context.getContentStream().newLineAtOffset(
                    context.getMarginLeft(),
                    context.getCursorY()
            );
            context.getContentStream().showText(line);
            context.getContentStream().endText();
            context.moveCursorDown(lineHeight);
        }

        context.moveCursorDown(fontSize + 8);
    }

    private List<String> wrapText(String content, float availableWidth, PDType0Font font, float fontSize) throws IOException {
        List<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();

        for (String word : content.split("\\s+")) {
            String temp = line.isEmpty() ? word : line + " " + word;

            float textWidth = font.getStringWidth(temp) / 1000 * fontSize;

            if (textWidth <= availableWidth) {
                line.setLength(0);
                line.append(temp);
            } else {
                if (!line.isEmpty()) {
                    lines.add(line.toString());
                    line.setLength(0);
                    line.append(word);
                } else {
                    lines.add(word);
                }
            }
        }
        if (!line.isEmpty()) {
            lines.add(line.toString());
        }
        return lines;
    }

    private PDType0Font resolveFont(PdfElement element, FontSet fontSet) {
        if (element.bold() && element.italic()) {
            return fontSet.boldAndItalic();
        }

        if (element.bold()) {
            return fontSet.bold();
        }

        if (element.italic()) {
            return fontSet.italic();
        }

        return fontSet.regular();
    }
}
