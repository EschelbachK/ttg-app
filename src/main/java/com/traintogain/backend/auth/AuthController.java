package com.traintogain.backend.auth;

import com.traintogain.backend.api.ApiResponse;
import com.traintogain.backend.auth.dto.*;
import com.traintogain.backend.auth.refreshtoken.RefreshToken;
import com.traintogain.backend.auth.refreshtoken.RefreshTokenService;
import com.traintogain.backend.training.TrainingPlan;
import com.traintogain.backend.training.TrainingPlanRepository;
import com.traintogain.backend.user.User;
import com.traintogain.backend.user.UserService;
import com.traintogain.backend.user.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final TrainingPlanRepository trainingPlanRepository;

    public AuthController(
            UserService userService,
            JwtService jwtService,
            RefreshTokenService refreshTokenService,
            TrainingPlanRepository trainingPlanRepository
    ) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.trainingPlanRepository = trainingPlanRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {

        User user = userService.login(
                request.email(),
                request.password()
        );

        ensureTrainingPlan(user.getId());

        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user.getId());

        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole().name()
        );

        AuthResponse response = new AuthResponse(
                accessToken,
                refreshToken.getToken(),
                userResponse
        );

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@RequestBody RefreshTokenRequest request) {

        RefreshToken refreshToken =
                refreshTokenService.validateRefreshToken(request.refreshToken());

        String userId = refreshToken.getUserId();

        User user = userService.getById(userId);
        String newAccessToken = jwtService.generateAccessToken(user);

        RefreshToken newRefreshToken =
                refreshTokenService.createRefreshToken(userId);

        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole().name()
        );

        AuthResponse response = new AuthResponse(
                newAccessToken,
                newRefreshToken.getToken(),
                userResponse
        );

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@RequestBody RegisterRequest request) {

        User user = userService.register(
                request.email(),
                request.username(),
                request.password()
        );

        ensureTrainingPlan(user.getId());

        UserResponse response = new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole().name()
        );

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestBody LogoutRequest request) {

        refreshTokenService.deleteByToken(request.getRefreshToken());

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    private void ensureTrainingPlan(String userId) {
        boolean exists = trainingPlanRepository.existsByUserId(userId);

        if (!exists) {
            TrainingPlan plan = new TrainingPlan();
            plan.setUserId(userId);
            trainingPlanRepository.save(plan);
        }
    }
}