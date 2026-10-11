package com.backend.pdf_wizard.service.accountServices;

import com.backend.pdf_wizard.dao.entity.Account;
import com.backend.pdf_wizard.dao.repository.jpa.AccountRepository;
import com.backend.pdf_wizard.exception.ResourceNotFountException;
import com.backend.pdf_wizard.model.dto.accountDtos.AccountResponseDto;
import com.backend.pdf_wizard.model.dto.accountDtos.ResetPasswordRequestDto;
import com.backend.pdf_wizard.model.dto.accountDtos.UpdateAccountDetailsRequestDto;
import com.backend.pdf_wizard.model.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountMapper accountMapper;

    @Override
    @Transactional(readOnly = true)
    public AccountResponseDto getAccountById(UUID accountId) {
        return accountMapper.toDto(accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFountException("Account not found")));
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponseDto getAccountByEmail(String email) {
        return accountMapper.toDto(accountRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFountException("Account not found")));
    }

    @Override
    @Transactional
    public AccountResponseDto updateAccountDetails(UUID accountId, UpdateAccountDetailsRequestDto request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFountException("Account not found"));

        if (request.email() != null && !request.email().isBlank() && !account.getEmail().equals(request.email())) {
            account.setEmail(request.email());
        }

        if (request.name() != null && !request.name().isBlank() && !account.getName().equals(request.name())) {
            account.setName(request.name());
        }

        return accountMapper.toDto(accountRepository.save(account));
    }

    @Override
    @Transactional
    public void resetPassword(UUID accountId, ResetPasswordRequestDto request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFountException("Account not found"));

        if (!passwordEncoder.matches(request.currentPassword(), account.getPassword())) {
            throw new BadCredentialsException("Current password does not match");
        }

        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new BadCredentialsException("Confirmation password does not match new password");
        }

        account.setPassword(passwordEncoder.encode(request.newPassword()));
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public void deleteAccount(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFountException("Account not found"));
        accountRepository.delete(account);
    }
}
