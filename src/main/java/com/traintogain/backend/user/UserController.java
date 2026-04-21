package com.traintogain.backend.user;

import com.traintogain.backend.auth.refreshtoken.RefreshTokenService;
import com.traintogain.backend.user.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    public UserController(UserService userService, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
    }

    @GetMapping("/me")
    public UserProfileResponse me(Authentication auth) {
        return UserMapper.toProfile(userService.getById(auth.getName()));
    }

    @PatchMapping("/me")
    public UserProfileResponse updateMe(Authentication auth, @RequestBody UpdateUserProfileRequest r) {
        return UserMapper.toProfile(
                userService.updateProfile(auth.getName(), r.email(), r.username())
        );
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(Authentication auth, @RequestBody ChangePasswordRequest r) {
        userService.changePassword(auth.getName(), r.oldPassword(), r.newPassword());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> delete(Authentication auth) {
        String id = auth.getName();
        refreshTokenService.deleteTokensForUser(id);
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}