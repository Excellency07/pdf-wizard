package com.backend.pdf_wizard.model.dto;

import java.time.Instant;

public record ErrorResponseDto(
        Instant timestamp,
        int status,
        String error,
        Object details
) {
}
