package com.backend.pdf_wizard.model.dto.authDtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResendVerificationLinkRequestDto(
        @NotBlank
        @Email
        String email
) {
    public ResendVerificationLinkRequestDto {
        email = (email != null) ? email.trim() : null;
    }
}
