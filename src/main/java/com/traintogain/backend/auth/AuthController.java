package com.traintogain.backend.auth;

import com.traintogain.backend.api.ApiResponse;
import com.traintogain.backend.auth.dto.*;
import com.traintogain.backend.auth.refreshtoken.RefreshToken;
import com.traintogain.backend.auth.refreshtoken.RefreshTokenService;
import com.traintogain.backend.user.User;
import com.traintogain.backend.user.UserService;
import com.traintogain.backend.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(UserService userService, JwtService jwtService,
                          RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody @Valid LoginRequest r) {
        User user = userService.login(r.email(), r.password());
        return ResponseEntity.ok(ApiResponse.success(buildAuthResponse(user)));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody @Valid RegisterRequest r) {
        User user = userService.register(r.email(), r.username(), r.password());
        return ResponseEntity.ok(ApiResponse.success(buildAuthResponse(user)));
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