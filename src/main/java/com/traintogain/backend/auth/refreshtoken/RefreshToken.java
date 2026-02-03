package com.traintogain.backend.auth.refreshtoken;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "refresh_tokens")
public class RefreshToken {

    @Id
    private String id;

    private String userId;
    private String token;
    private Instant expiresAt;

    public RefreshToken() {
    }

    public RefreshToken(String userId, String token, Instant expiresAt) {
        this.userId = userId;
        this.token = token;
        this.expiresAt = expiresAt;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
}