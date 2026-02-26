package com.traintogain.backend.user.dto;

public record UserResponse(
        String id,
        String email,
        String username,
        String role
) {}