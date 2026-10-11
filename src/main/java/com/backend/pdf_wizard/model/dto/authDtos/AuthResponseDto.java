package com.backend.pdf_wizard.model.dto.authDtos;

public record AuthResponseDto (
        String token,
        String refreshToken,
        String tokenType,
        String email,
        long expiresIn
){
    public AuthResponseDto(String accessToken, String refreshToken, String email, long expiresIn) {
        this(accessToken, refreshToken, "bearer", email, expiresIn);
    }
}
