package com.traintogain.backend.auth.dto;

public record RefreshTokenRequest(
        String refreshToken
) {}