package com.traintogain.backend.passwordreset.dto;

public record PasswordResetConfirm(String token, String newPassword) {}