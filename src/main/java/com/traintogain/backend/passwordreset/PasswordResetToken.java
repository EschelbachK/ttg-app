package com.traintogain.backend.passwordreset;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    private String id;

    private String userId;
    private String token;
    private Instant expiresAt;

    public PasswordResetToken(
            String userId,
            String token,
            Instant expiresAt
    ) {
        this.userId = userId;
        this.token = token;
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;
    }
}
