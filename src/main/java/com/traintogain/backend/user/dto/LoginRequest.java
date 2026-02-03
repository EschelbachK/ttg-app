package com.traintogain.backend.user.dto;

public record LoginRequest(
        String email,
        String password
) {}