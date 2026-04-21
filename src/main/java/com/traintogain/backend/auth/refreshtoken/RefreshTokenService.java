package com.traintogain.backend.auth.refreshtoken;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private static final long EXP = 60 * 60 * 24 * 7;

    private final RefreshTokenRepository repo;

    public RefreshTokenService(RefreshTokenRepository repo) {
        this.repo = repo;
    }

    public RefreshToken createRefreshToken(String userId) {
        repo.deleteByUserId(userId);
        return repo.save(new RefreshToken(
                userId,
                UUID.randomUUID().toString(),
                Instant.now().plusSeconds(EXP)
        ));
    }

    public RefreshToken validateRefreshToken(String token) {
        RefreshToken rt = repo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        if (rt.isExpired()) {
            repo.delete(rt);
            throw new RuntimeException("Token expired");
        }

        return rt;
    }

    public void deleteByToken(String token) {
        repo.findByToken(token).ifPresent(repo::delete);
    }

    public void deleteTokensForUser(String userId) {
        repo.deleteByUserId(userId);
    }
}