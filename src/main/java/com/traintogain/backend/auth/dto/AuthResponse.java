package com.traintogain.backend.auth.dto;

import com.traintogain.backend.user.dto.UserResponse;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        UserResponse user
) {}