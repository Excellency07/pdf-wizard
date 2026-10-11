package com.backend.pdf_wizard.service.pdfServices.layout;

import com.backend.pdf_wizard.model.dto.pdfDtos.PdfElement;
import com.backend.pdf_wizard.model.dto.pdfDtos.fontDtos.FontSet;
import com.backend.pdf_wizard.model.enums.ElementType;
import com.backend.pdf_wizard.service.pdfServices.renderer.ElementRenderer;
import com.backend.pdf_wizard.service.pdfServices.renderer.HeadingRenderer;
import com.backend.pdf_wizard.service.pdfServices.renderer.TextRenderer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class PdfLayoutManager {
    private final Map<ElementType, ElementRenderer> renderers;

    public PdfLayoutManager(HeadingRenderer headingRenderer, TextRenderer textRenderer) {
        this.renderers = Map.of(
                ElementType.HEADING, headingRenderer,
                ElementType.TEXT, textRenderer
        );
    }

    public void renderElements(
            List<PdfElement> elements,
            LayoutContext context,
            FontSet fontSet
    ) throws IOException {
        for (PdfElement element : elements) {
            ElementRenderer renderer = renderers.get(element.type());

            if (renderer == null) {
                throw new IllegalArgumentException("Element type not found");
            }
            renderer.render(element, context, fontSet);
        }

    }
}
