package com.backend.pdf_wizard.model.dto.accountDtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;


public record UpdateAccountDetailsRequestDto(
        @Size(min = 2, max = 100, message = "Name field must be between 2 and 100 characters")
        String name,

        @Email
        String email
) {
    public UpdateAccountDetailsRequestDto {
        name = (name != null) ? name.trim() : null;
        email = (email != null) ? email.trim() : null;
    }
}
