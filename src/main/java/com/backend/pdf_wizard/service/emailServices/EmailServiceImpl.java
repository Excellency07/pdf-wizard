package com.backend.pdf_wizard.service.emailServices;

import com.backend.pdf_wizard.dao.entity.PdfMetaData;
import com.backend.pdf_wizard.dao.repository.jpa.PdfDocumentRepository;
import com.backend.pdf_wizard.exception.ResourceNotFountException;
import com.backend.pdf_wizard.service.storageServices.StorageService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final PdfDocumentRepository pdfDocumentRepository;
    private final StorageService storageService;

    @Override
    public void sendVerificationMessage(String to, String verificationToken) {
        SimpleMailMessage mail =  new SimpleMailMessage();
        mail.setTo(to);
        mail.setSubject("Activate Your Account: ");
        mail.setText("Click the link to activate your account:\n" +
                "http://localhost:8080/api/auth/activate?token=" + verificationToken);

        mailSender.send(mail);
    }

    @Override
    public void sendPdf(UUID accountId, UUID pdfId, String to) {
        PdfMetaData pdf = pdfDocumentRepository.findByIdAndAccountId(pdfId, accountId)
                .orElseThrow(() -> new ResourceNotFountException("PDF not found"));

        try (InputStream inputStream =
                     storageService.download(
                             pdf.getStorageName(),
                             pdf.getFilename()
                     )) {

            byte[] pdfBytes = inputStream.readAllBytes();

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Your PDF: " + pdf.getFilename());
            helper.setText("PDF: ");

            InputStreamSource attachment = () ->
                    new java.io.ByteArrayInputStream(pdfBytes);

            helper.addAttachment(pdf.getFilename(), attachment);

            mailSender.send(message);

        } catch (MessagingException | IOException e) {
            throw new RuntimeException("Failed to send PDF email", e);
        }
    }
}
