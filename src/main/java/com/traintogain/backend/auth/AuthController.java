package com.traintogain.backend.auth;

import com.traintogain.backend.api.ApiResponse;
import com.traintogain.backend.auth.dto.*;
import com.traintogain.backend.auth.refreshtoken.RefreshToken;
import com.traintogain.backend.auth.refreshtoken.RefreshTokenService;
import com.traintogain.backend.user.User;
import com.traintogain.backend.user.UserService;
import com.traintogain.backend.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final String frontendUrl;

    public AuthController(
            UserService userService,
            JwtService jwtService,
            RefreshTokenService refreshTokenService,
            @Value("${frontend.url}") String frontendUrl
    ) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.frontendUrl = frontendUrl;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody @Valid LoginRequest r) {
        User user = userService.login(r.email(), r.password());
        return ResponseEntity.ok(ApiResponse.success(buildAuthResponse(user)));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody @Valid RegisterRequest r) {
        userService.register(r.email(), r.username(), r.password());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@RequestBody @Valid RefreshTokenRequest r) {
        RefreshToken token = refreshTokenService.validateRefreshToken(r.refreshToken());
        User user = userService.getById(token.getUserId());
        return ResponseEntity.ok(ApiResponse.success(buildAuthResponse(user)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestBody @Valid LogoutRequest r) {
        refreshTokenService.deleteByToken(r.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@RequestBody @Valid ForgotPasswordRequest r) {
        userService.forgotPassword(r.email());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestBody @Valid ResetPasswordRequest r) {
        userService.resetPassword(r.token(), r.newPassword());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam String token) {
        userService.verifyEmail(token);
        return ResponseEntity.ok("email verified");
    }

    @GetMapping("/reset-password")
    public ResponseEntity<String> resetPage(@RequestParam String token) {
        return ResponseEntity.ok("reset token: " + token);
    }

    private AuthResponse buildAuthResponse(User user) {
        String access = jwtService.generateAccessToken(user);
        RefreshToken refresh = refreshTokenService.createRefreshToken(user.getId());
        return new AuthResponse(access, refresh.getToken(), mapUser(user));
    }

    private UserResponse mapUser(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole().name()
        );
    }
}