package com.traintogain.backend.auth.dto;

public record LoginRequest(
        String email,
        String password
) {}