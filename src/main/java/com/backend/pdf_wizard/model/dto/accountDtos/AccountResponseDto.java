package com.backend.pdf_wizard.model.dto.accountDtos;

import java.time.Instant;
import java.util.UUID;

public record AccountResponseDto(
        UUID id,
        String name,
        String email,
        Instant createdAt
) {
}
