package com.backend.pdf_wizard.service.accountServices;

import com.backend.pdf_wizard.model.dto.accountDtos.*;
import com.backend.pdf_wizard.model.dto.authDtos.AuthResponseDto;
import com.backend.pdf_wizard.model.dto.authDtos.LoginRequestDto;
import com.backend.pdf_wizard.model.dto.authDtos.LogoutRequestDto;
import com.backend.pdf_wizard.model.dto.authDtos.RegisterAccountRequestDto;


public interface AuthService {
    AccountResponseDto register(RegisterAccountRequestDto request);

    void activateAccount(String verificationToken);

    void resendVerificationLink(String email);

    AuthResponseDto verifyLogin(LoginRequestDto request);

    void logout(LogoutRequestDto request);
}
