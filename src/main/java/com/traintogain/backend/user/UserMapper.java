package com.traintogain.backend.user;

import com.traintogain.backend.user.dto.UserProfileResponse;
import com.traintogain.backend.user.dto.UserResponse;

public class UserMapper {

    public static UserResponse toResponse(User u) {
        return new UserResponse(
                u.getId(),
                u.getEmail(),
                u.getUsername(),
                u.getRole().name()
        );
    }

    public static UserProfileResponse toProfile(User u) {
        return new UserProfileResponse(
                u.getId(),
                u.getEmail(),
                u.getUsername(),
                u.getRole().name()
        );
    }
}