package com.backend.pdf_wizard.dao.repository.redis;

import com.backend.pdf_wizard.dao.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String> {
    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByAccountId(UUID accountId);
}
