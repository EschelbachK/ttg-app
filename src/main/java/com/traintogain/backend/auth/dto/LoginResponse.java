package com.traintogain.backend.auth.dto;

public record LoginResponse(
        String token,
        String userId,
        String username,
        String email
) {}