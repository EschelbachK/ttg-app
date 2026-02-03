package com.traintogain.backend.user.dto;

public record ChangePasswordRequest(
        String oldPassword,
        String newPassword
) {}