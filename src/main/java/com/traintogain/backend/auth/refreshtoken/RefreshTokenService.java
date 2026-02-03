package com.traintogain.backend.auth.refreshtoken;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    // ⏱️ 7 Tage
    private static final long REFRESH_TOKEN_EXPIRATION_SECONDS =
            60 * 60 * 24 * 7;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * Erstellt ein neues Refresh Token für einen User
     */
    public RefreshToken createRefreshToken(String userId) {

        // 🔄 Alte Tokens des Users löschen (1 aktives Token)
        refreshTokenRepository.deleteByUserId(userId);

        RefreshToken refreshToken = new RefreshToken(
                userId,
                UUID.randomUUID().toString(),
                Instant.now().plusSeconds(REFRESH_TOKEN_EXPIRATION_SECONDS)
        );

        return refreshTokenRepository.save(refreshToken);
    }

    /**
     * Validiert ein Refresh Token
     */
    public RefreshToken validateRefreshToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() ->
                        new RuntimeException("Refresh token not found")
                );

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired");
        }

        return refreshToken;
    }

    /**
     * Löscht alle Refresh Tokens eines Users (Logout)
     */
    public void deleteTokensForUser(String userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}