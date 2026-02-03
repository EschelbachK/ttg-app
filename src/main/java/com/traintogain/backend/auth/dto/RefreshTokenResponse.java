package com.traintogain.backend.auth.dto;

public record RefreshTokenResponse(
        String accessToken,
        String refreshToken
) {}