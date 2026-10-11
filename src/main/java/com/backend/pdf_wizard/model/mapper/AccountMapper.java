package com.backend.pdf_wizard.model.mapper;

import com.backend.pdf_wizard.dao.entity.Account;
import com.backend.pdf_wizard.model.dto.accountDtos.AccountResponseDto;
import com.backend.pdf_wizard.model.dto.authDtos.RegisterAccountRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Account toEntity(RegisterAccountRequestDto request);

    AccountResponseDto toDto(Account account);
}
