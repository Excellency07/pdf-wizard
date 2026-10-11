package com.backend.pdf_wizard.service.accountServices;

import com.backend.pdf_wizard.dao.entity.RefreshToken;
import com.backend.pdf_wizard.model.dto.tokenDtos.RefreshTokenResponseDto;

import java.util.UUID;

public interface RefreshTokenService {
    RefreshToken generateRefreshToken(UUID accountId);
    RefreshTokenResponseDto refreshToken(String requestRefreshToken);
    RefreshToken rotateRefreshToken(String refreshToken);
    void logout(String refreshToken);
}
