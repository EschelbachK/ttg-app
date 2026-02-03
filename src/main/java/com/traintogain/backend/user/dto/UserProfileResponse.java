package com.traintogain.backend.user.dto;

public record UserProfileResponse(
        String id,
        String email,
        String username,
        String role
) {}