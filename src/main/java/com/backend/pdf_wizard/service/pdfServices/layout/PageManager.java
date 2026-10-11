package com.backend.pdf_wizard.service.pdfServices.layout;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PageManager {
    public void ensureSpace(
            LayoutContext context,
            float requiredHeight
    ) throws IOException {

        float lowestPosition = context.getCursorY() - requiredHeight;

        if (lowestPosition < context.getAvailableBottom()) {
            context.startNewPage();
        }
    }
}
