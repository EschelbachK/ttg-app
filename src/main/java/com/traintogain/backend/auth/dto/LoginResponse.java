package com.traintogain.backend.auth.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String userId,
        String username,
        String email
) {}
