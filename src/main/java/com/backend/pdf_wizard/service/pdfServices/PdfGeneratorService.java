package com.backend.pdf_wizard.service.pdfServices;

import com.backend.pdf_wizard.model.dto.pdfDtos.GeneratePdfRequestDto;

public interface PdfGeneratorService {
    byte[] generate(GeneratePdfRequestDto request);
}
