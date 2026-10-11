package com.backend.pdf_wizard.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SendPdfEmailRequestDto(
        @NotBlank @Email String to
) {
}