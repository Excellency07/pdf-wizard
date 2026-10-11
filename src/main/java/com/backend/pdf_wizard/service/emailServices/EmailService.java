package com.backend.pdf_wizard.service.emailServices;

import java.util.UUID;

public interface EmailService {
    void sendVerificationMessage(String to, String verificationToken);
    void sendPdf(UUID accountId, UUID pdfId, String to);
}
