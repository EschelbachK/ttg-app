package com.traintogain.backend.passwordreset.dto;

public record ConfirmPasswordResetRequest(
        String token,
        String newPassword
) {}