package com.backend.pdf_wizard.model.dto.tokenDtos;

public record RefreshTokenResponseDto(
        String accessToken,
        String refreshToken
) {
}
