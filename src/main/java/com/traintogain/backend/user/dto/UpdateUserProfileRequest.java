package com.traintogain.backend.user.dto;

public record UpdateUserProfileRequest(
        String email,
        String username
) {}