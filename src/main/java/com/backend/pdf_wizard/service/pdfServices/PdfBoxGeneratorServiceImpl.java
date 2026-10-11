package com.backend.pdf_wizard.service.pdfServices;

import com.backend.pdf_wizard.model.dto.pdfDtos.GeneratePdfRequestDto;
import com.backend.pdf_wizard.model.dto.pdfDtos.fontDtos.FontSet;
import com.backend.pdf_wizard.model.enums.PageFormat;
import com.backend.pdf_wizard.model.enums.PdfOrientation;
import com.backend.pdf_wizard.service.pdfServices.fontServices.FontManager;
import com.backend.pdf_wizard.service.pdfServices.layout.LayoutContext;
import com.backend.pdf_wizard.service.pdfServices.layout.PdfLayoutManager;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PdfBoxGeneratorServiceImpl implements PdfGeneratorService {

    private final PdfLayoutManager pdfLayoutManager;
    private final FontManager fontManager;

    @Override
    public byte[] generate(GeneratePdfRequestDto request) {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        ) {
            PDPage page = new PDPage(resolvePageFormat(request.format(), request.orientation()));
            document.addPage(page);
            PDDocumentInformation information = new PDDocumentInformation();
            if (request.title() != null) {
                information.setTitle(request.title());
            }

            if (request.author() != null) {
                information.setAuthor(request.author());
            }

            FontSet fontSet = fontManager.loadFonts(document);

            try (
                    PDPageContentStream contentStream = new PDPageContentStream(document, page)
            ) {
                LayoutContext context = new LayoutContext(
                        document,
                        page,
                        contentStream
                );

                pdfLayoutManager.renderElements(
                        request.elements(),
                        context,
                        fontSet
                );
                context.getContentStream().close();
            }
            document.save(outputStream);
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private PDRectangle resolvePageFormat(PageFormat format, PdfOrientation orientation) {
        PDRectangle rectangle = switch (format) {
            case A5 -> PDRectangle.A5;
            default -> PDRectangle.A4;
        };

        if (orientation == PdfOrientation.LANDSCAPE) {
            return new PDRectangle(rectangle.getHeight(), rectangle.getWidth());
        }

        return rectangle;
    }

}
