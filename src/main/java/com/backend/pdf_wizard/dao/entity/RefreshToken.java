package com.backend.pdf_wizard.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("refresh_tokens")
public class RefreshToken {
    @Id
    private String token;

    @Indexed
    private String familyId;

    @Indexed
    private UUID accountId;

    private boolean used = false;

    @TimeToLive
    private Long expirationTime;

    public RefreshToken(String token, String familyId, UUID accountId, Long expirationTime) {
        this.token = token;
        this.familyId = familyId;
        this.accountId = accountId;
        this.expirationTime = expirationTime;
    }
}
