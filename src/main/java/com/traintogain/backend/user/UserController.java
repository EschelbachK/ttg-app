package com.traintogain.backend.user;

import com.traintogain.backend.user.dto.UpdateUserProfileRequest;
import com.traintogain.backend.user.dto.UserProfileResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserProfileResponse me(Authentication authentication) {

        String userId = authentication.getName(); // 👈 kommt aus JWT (sub)

        User user = userService.getById(userId);

        return new UserProfileResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole().name()
        );
    }

    @PatchMapping("/me")
    public UserProfileResponse updateMe(
            Authentication authentication,
            @RequestBody UpdateUserProfileRequest request
    ) {
        String userId = authentication.getName();

        User updatedUser = userService.updateProfile(
                userId,
                request.email(),
                request.username()
        );

        return new UserProfileResponse(
                updatedUser.getId(),
                updatedUser.getEmail(),
                updatedUser.getUsername(),
                updatedUser.getRole().name()
        );
    }

}
