package com.backend.pdf_wizard.controller;

import com.backend.pdf_wizard.model.dto.accountDtos.AccountResponseDto;
import com.backend.pdf_wizard.model.dto.accountDtos.ResetPasswordRequestDto;
import com.backend.pdf_wizard.model.dto.accountDtos.UpdateAccountDetailsRequestDto;
import com.backend.pdf_wizard.security.AccountDetails;
import com.backend.pdf_wizard.service.accountServices.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<AccountResponseDto> getProfile(@AuthenticationPrincipal AccountDetails accountDetails) {
        UUID id = accountDetails.getId();
        AccountResponseDto accountResponseDto = accountService.getAccountById(id);
        return ResponseEntity.ok(accountResponseDto);
    }

    @PatchMapping
    public ResponseEntity<AccountResponseDto> updateAccountDetails(@AuthenticationPrincipal AccountDetails accountDetails,
                                                                   @Valid @RequestBody UpdateAccountDetailsRequestDto request) {
        UUID id = accountDetails.getId();
        AccountResponseDto accountResponseDto = accountService.updateAccountDetails(id, request);
        return ResponseEntity.ok(accountResponseDto);
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@AuthenticationPrincipal AccountDetails accountDetails,
                                              @Valid @RequestBody ResetPasswordRequestDto request){
        UUID id = accountDetails.getId();
        accountService.resetPassword(id, request);
        return ResponseEntity.ok("Password changed");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAccount(@AuthenticationPrincipal AccountDetails accountDetails){
        UUID id = accountDetails.getId();
        accountService.deleteAccount(id);
        return ResponseEntity.ok("Account deleted");
    }
}
