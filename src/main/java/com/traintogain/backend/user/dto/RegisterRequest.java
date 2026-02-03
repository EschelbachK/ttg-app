package com.traintogain.backend.user.dto;

public record RegisterRequest(
        String email,
        String username,
        String password
) {}