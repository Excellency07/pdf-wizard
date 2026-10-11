package com.backend.pdf_wizard.service.pdfServices.layout;

import lombok.Getter;
import org.apache.pdfbox.contentstream.PDContentStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.io.IOException;

@Getter
public class LayoutContext {

    private final PDDocument document;
    private PDPage page;
    private PDPageContentStream contentStream;

    private float pageWidth;
    private float pageHeight;

    private final float marginLeft = 50;
    private final float marginRight = 50;
    private final float marginTop = 50;
    private final float marginBottom = 50;

    private float cursorY;

    public LayoutContext(
            PDDocument document,
            PDPage page,
            PDPageContentStream contentStream
    ) {
        this.document = document;
        this.page = page;
        this.contentStream = contentStream;

        this.pageWidth = page.getMediaBox().getWidth();
        this.pageHeight = page.getMediaBox().getHeight();

        this.cursorY = pageHeight - marginTop;
    }

    public void startNewPage() throws IOException {
        contentStream.close();
        PDRectangle pageSize = page.getMediaBox();
        PDPage newPage = new PDPage(pageSize);
        document.addPage(newPage);

        PDPageContentStream newContentStream = new PDPageContentStream(document, newPage);

        this.page = newPage;
        this.contentStream = newContentStream;

        updatePageDimensions();
        this.cursorY = pageHeight - marginTop;
    }

    private void updatePageDimensions() {
        PDRectangle mediaBox = page.getMediaBox();

        this.pageWidth = mediaBox.getWidth();
        this.pageHeight = mediaBox.getHeight();
    }

    public void moveCursorDown(float amount) {
        cursorY -= amount;
    }

    public float getAvailableWidth() {
        return pageWidth - (marginLeft + marginRight);
    }

    public float getAvailableBottom() {
        return marginBottom;
    }
}
