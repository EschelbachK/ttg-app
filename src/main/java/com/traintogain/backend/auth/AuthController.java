package com.traintogain.backend.auth;

import com.traintogain.backend.auth.dto.LoginRequest;
import com.traintogain.backend.auth.dto.LoginResponse;
import com.traintogain.backend.user.User;
import com.traintogain.backend.user.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(
            UserService userService,
            JwtService jwtService
    ) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        User user = userService.login(
                request.email(),
                request.password()
        );

        String token = jwtService.generateToken(user.getId());

        return new LoginResponse(
                token,
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}