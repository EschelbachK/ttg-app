package com.traintogain.backend.auth;

import com.traintogain.backend.auth.dto.LoginRequest;
import com.traintogain.backend.auth.dto.LoginResponse;
import com.traintogain.backend.auth.dto.RefreshTokenRequest;
import com.traintogain.backend.auth.dto.RefreshTokenResponse;
import com.traintogain.backend.auth.dto.RegisterRequest;
import com.traintogain.backend.auth.refreshtoken.RefreshToken;
import com.traintogain.backend.auth.refreshtoken.RefreshTokenService;
import com.traintogain.backend.user.User;
import com.traintogain.backend.user.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(
            UserService userService,
            JwtService jwtService,
            RefreshTokenService refreshTokenService
    ) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    // 🔐 LOGIN
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        User user = userService.login(
                request.email(),
                request.password()
        );

        String accessToken = jwtService.generateToken(user.getId());
        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user.getId());

        return new LoginResponse(
                accessToken,
                refreshToken.getToken(),
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }

    // 🆕 REGISTER
    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        return userService.register(
                request.email(),
                request.username(),
                request.password()
        );
    }

    // 🔄 REFRESH TOKEN
    @PostMapping("/refresh")
    public RefreshTokenResponse refresh(
            @RequestBody RefreshTokenRequest request
    ) {
        RefreshToken refreshToken =
                refreshTokenService.validateRefreshToken(
                        request.refreshToken()
                );

        String userId = refreshToken.getUserId();

        String newAccessToken =
                jwtService.generateToken(userId);

        RefreshToken newRefreshToken =
                refreshTokenService.createRefreshToken(userId);

        return new RefreshTokenResponse(
                newAccessToken,
                newRefreshToken.getToken()
        );
    }
}