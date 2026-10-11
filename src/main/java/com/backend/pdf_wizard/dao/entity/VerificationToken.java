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
@RedisHash("verification_token")
public class VerificationToken {
    @Id
    private String token;

    @Indexed
    private UUID accountId;

    @TimeToLive
    private Long expirationTime;
}
