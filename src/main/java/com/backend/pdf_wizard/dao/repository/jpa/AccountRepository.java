package com.backend.pdf_wizard.dao.repository.jpa;

import com.backend.pdf_wizard.dao.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);
}
