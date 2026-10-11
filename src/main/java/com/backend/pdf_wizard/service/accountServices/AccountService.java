package com.backend.pdf_wizard.service.accountServices;

import com.backend.pdf_wizard.model.dto.accountDtos.AccountResponseDto;
import com.backend.pdf_wizard.model.dto.accountDtos.ResetPasswordRequestDto;
import com.backend.pdf_wizard.model.dto.accountDtos.UpdateAccountDetailsRequestDto;

import java.util.UUID;

public interface AccountService {
    AccountResponseDto getAccountById(UUID accountId);
    AccountResponseDto getAccountByEmail(String email);
    AccountResponseDto updateAccountDetails(UUID accountId, UpdateAccountDetailsRequestDto request);
    void resetPassword(UUID accountId, ResetPasswordRequestDto request);
    void deleteAccount(UUID accountId);
}
