package com.backend.pdf_wizard.service.accountServices;

import com.backend.pdf_wizard.dao.entity.RefreshToken;
import com.backend.pdf_wizard.dao.entity.VerificationToken;
import com.backend.pdf_wizard.dao.repository.redis.VerificationTokenRepository;
import com.backend.pdf_wizard.exception.ResourceAlreadyExistsException;
import com.backend.pdf_wizard.dao.entity.Account;
import com.backend.pdf_wizard.dao.repository.jpa.AccountRepository;
import com.backend.pdf_wizard.exception.ResourceNotFountException;
import com.backend.pdf_wizard.model.dto.accountDtos.*;
import com.backend.pdf_wizard.model.dto.authDtos.AuthResponseDto;
import com.backend.pdf_wizard.model.dto.authDtos.LoginRequestDto;
import com.backend.pdf_wizard.model.dto.authDtos.LogoutRequestDto;
import com.backend.pdf_wizard.model.dto.authDtos.RegisterAccountRequestDto;
import com.backend.pdf_wizard.model.mapper.AccountMapper;
import com.backend.pdf_wizard.security.AccountDetails;
import com.backend.pdf_wizard.security.JwtService;
import com.backend.pdf_wizard.service.emailServices.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountMapper accountMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final EmailService emailService;
    private final VerificationTokenRepository verificationTokenRepository;

    @Override
    public AccountResponseDto register(RegisterAccountRequestDto request) {
        if (accountRepository.existsByEmail(request.email()))
            throw new ResourceAlreadyExistsException("Account already registered");

        Account account = accountMapper.toEntity(request);
        account.setPassword(passwordEncoder.encode(request.password()));
        accountRepository.save(account);

        sendVerificationLink(account);
        return accountMapper.toDto(account);
    }

    @Override
    public void activateAccount(String verificationToken) {
        VerificationToken requestVerificationToken = verificationTokenRepository.findByToken(verificationToken)
                .orElseThrow(()-> new ResourceNotFountException("Verification token not found or expired"));

        Account account = accountRepository.findById(requestVerificationToken.getAccountId())
                .orElseThrow(()-> new ResourceNotFountException("Account not found"));

        account.setEnabled(true);
        accountRepository.save(account);

        verificationTokenRepository.delete(requestVerificationToken);
    }

    @Override
    public void resendVerificationLink(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFountException("Account not found"));

        sendVerificationLink(account);
    }

    @Override
    public AuthResponseDto verifyLogin(LoginRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        AccountDetails accountDetails = (AccountDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(Objects.requireNonNull(accountDetails));
        RefreshToken refreshToken = refreshTokenService.generateRefreshToken(accountDetails.getId());
        return new AuthResponseDto(
                token,
                refreshToken.getToken(),
                accountDetails.getUsername(),
                jwtService.getEXPIRATION_TIME()
        );

    }

    @Override
    public void logout(LogoutRequestDto request) {
    }

    private void sendVerificationLink(Account account) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, account.getId(), 86400L);
        verificationTokenRepository.save(verificationToken);
        emailService.sendVerificationMessage(account.getEmail(), verificationToken.getToken());
    }
}
