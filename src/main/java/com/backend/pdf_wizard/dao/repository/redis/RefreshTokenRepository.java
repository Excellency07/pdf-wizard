package com.backend.pdf_wizard.dao.repository.redis;

import com.backend.pdf_wizard.dao.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByToken(String refreshToken);

    List<RefreshToken> findByFamilyId(String familyId);
}
