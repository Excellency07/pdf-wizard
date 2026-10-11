package com.backend.pdf_wizard.security;

import com.backend.pdf_wizard.dao.entity.Account;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class AccountDetails implements UserDetails {

    private final Account account;

    @Override
    public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public @Nullable String getPassword() {
        return account.getPassword();
    }

    @Override
    public @NonNull String getUsername() {
        return account.getEmail();
    }

    public UUID getId(){
        return account.getId();
    }

    @Override
    public boolean isEnabled() {
        return account.isEnabled();
    }
}
