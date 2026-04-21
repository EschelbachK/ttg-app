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

    public AuthController(UserService userService, JwtService jwtService,
                          RefreshTokenService refreshTokenService,
                          TrainingPlanRepository trainingPlanRepository) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.trainingPlanRepository = trainingPlanRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest r) {
        User user = userService.login(r.email(), r.password());
        ensureTrainingPlan(user.getId());
        return ResponseEntity.ok(ApiResponse.success(buildAuthResponse(user)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@RequestBody RefreshTokenRequest r) {
        RefreshToken token = refreshTokenService.validateRefreshToken(r.refreshToken());
        User user = userService.getById(token.getUserId());
        return ResponseEntity.ok(ApiResponse.success(buildAuthResponse(user)));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@RequestBody RegisterRequest r) {
        User user = userService.register(r.email(), r.username(), r.password());
        ensureTrainingPlan(user.getId());
        return ResponseEntity.ok(ApiResponse.success(mapUser(user)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestBody LogoutRequest r) {
        refreshTokenService.deleteByToken(r.getRefreshToken());
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

    private void ensureTrainingPlan(String userId) {
        if (trainingPlanRepository.existsByUserId(userId)) return;

        TrainingPlan plan = new TrainingPlan();
        plan.setUserId(userId);
        plan.setTitle("Mein erster Trainingsplan");
        plan.setArchived(false);

        trainingPlanRepository.save(plan);
    }
}