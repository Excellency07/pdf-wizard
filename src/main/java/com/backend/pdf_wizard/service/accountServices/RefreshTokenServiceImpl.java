package com.backend.pdf_wizard.service.accountServices;

import com.backend.pdf_wizard.dao.entity.Account;
import com.backend.pdf_wizard.exception.ResourceNotFountException;
import com.backend.pdf_wizard.dao.entity.RefreshToken;
import com.backend.pdf_wizard.dao.repository.jpa.AccountRepository;
import com.backend.pdf_wizard.dao.repository.redis.RefreshTokenRepository;
import com.backend.pdf_wizard.model.dto.tokenDtos.RefreshTokenResponseDto;
import com.backend.pdf_wizard.security.AccountDetails;
import com.backend.pdf_wizard.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final AccountRepository accountRepository;

    @Value("${jwt.refreshExpirationTime}")
    private long EXPIRATION_TIME;
    private final JwtService jwtService;

    @Override
    public RefreshToken generateRefreshToken(UUID accountId) {
        if (!accountRepository.existsById(accountId)) throw new ResourceNotFountException("Account not found");

        RefreshToken refreshToken = new RefreshToken(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                accountId,
                EXPIRATION_TIME
        );

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshTokenResponseDto refreshToken(String requestRefreshToken) {
        RefreshToken rotatedRefreshToken = rotateRefreshToken(requestRefreshToken);

        Account account = accountRepository.findById(rotatedRefreshToken.getAccountId())
                .orElseThrow(() -> new ResourceNotFountException("Account not found"));

        String accessToken = jwtService.generateToken(new AccountDetails(account));

        return new RefreshTokenResponseDto(accessToken, rotatedRefreshToken.getToken());
    }

    @Override
    public RefreshToken rotateRefreshToken(String refreshToken) {
        RefreshToken existingRefreshToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(()-> new ResourceNotFountException("Token not found or expired"));

        if (existingRefreshToken.isUsed()) {
            List<RefreshToken> family = refreshTokenRepository.findByFamilyId(existingRefreshToken.getFamilyId());
            refreshTokenRepository.deleteAll(family);
            throw new RuntimeException("Replay attack detected");
        }

        existingRefreshToken.setUsed(true);
        refreshTokenRepository.save(existingRefreshToken);

        RefreshToken newRefreshToken = new RefreshToken(
                UUID.randomUUID().toString(),
                existingRefreshToken.getFamilyId(),
                existingRefreshToken.getAccountId(),
                EXPIRATION_TIME
        );
        return refreshTokenRepository.save(newRefreshToken);
    }

    @Override
    public void logout(String refreshToken) {
        RefreshToken existingRefreshToken = refreshTokenRepository.findByToken(refreshToken).orElse(null);
        if (existingRefreshToken != null) {
            List<RefreshToken> family = refreshTokenRepository.findByFamilyId(existingRefreshToken.getFamilyId());
            refreshTokenRepository.deleteAll(family);
        }
    }
}
