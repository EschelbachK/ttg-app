package com.traintogain.backend.auth;

import com.traintogain.backend.user.User;
import com.traintogain.backend.user.UserService;
import com.traintogain.backend.user.dto.LoginRequest;
import com.traintogain.backend.user.dto.RegisterRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        return userService.register(
                request.email(),
                request.username(),
                request.password()
        );
    }

    @PostMapping("/login")
    public User login(@RequestBody LoginRequest request) {
        return userService.login(
                request.email(),
                request.password()
        );
    }
}