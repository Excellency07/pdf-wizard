package com.backend.pdf_wizard.controller;

import com.backend.pdf_wizard.model.dto.accountDtos.*;
import com.backend.pdf_wizard.model.dto.authDtos.AuthResponseDto;
import com.backend.pdf_wizard.model.dto.authDtos.LoginRequestDto;
import com.backend.pdf_wizard.model.dto.authDtos.LogoutRequestDto;
import com.backend.pdf_wizard.model.dto.authDtos.RegisterAccountRequestDto;
import com.backend.pdf_wizard.model.dto.tokenDtos.RefreshTokenRequestDto;
import com.backend.pdf_wizard.model.dto.tokenDtos.RefreshTokenResponseDto;
import com.backend.pdf_wizard.model.dto.authDtos.ResendVerificationLinkRequestDto;
import com.backend.pdf_wizard.service.accountServices.AuthService;
import com.backend.pdf_wizard.service.accountServices.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<AccountResponseDto> register(@Valid @RequestBody RegisterAccountRequestDto request) {
        AccountResponseDto accountResponseDto = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        AuthResponseDto authResponseDto = authService.verifyLogin(request);
        return ResponseEntity.ok(authResponseDto);
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam("token") String verificationToken) {
        authService.activateAccount(verificationToken);
        return ResponseEntity.ok("Account activated");
    }

    @PostMapping("/resend-verification-link")
    public ResponseEntity<String> resendVerificationLink(@Valid @RequestBody ResendVerificationLinkRequestDto request) {
        String destinationEmail = request.email();
        authService.resendVerificationLink(destinationEmail);
        return ResponseEntity.ok("Verification link resent");
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponseDto> refreshToken(@Valid @RequestBody RefreshTokenRequestDto request) {
        RefreshTokenResponseDto refreshTokenResponseDto = refreshTokenService.refreshToken(request.refreshToken());
        return ResponseEntity.ok(refreshTokenResponseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody LogoutRequestDto request) {
        refreshTokenService.logout(request.refreshToken());
        return ResponseEntity.ok("Logged out");
    }
}
