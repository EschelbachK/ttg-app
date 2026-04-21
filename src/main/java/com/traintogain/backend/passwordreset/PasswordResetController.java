package com.traintogain.backend.passwordreset;

import com.traintogain.backend.api.ApiResponse;
import com.traintogain.backend.passwordreset.PasswordResetService;
import com.traintogain.backend.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/password")
public class PasswordResetController {

    private final PasswordResetService service;
    private final UserService userService;

    public PasswordResetController(PasswordResetService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @PostMapping("/forgot")
    public ResponseEntity<ApiResponse<Void>> forgot(@RequestParam String email) {
        service.requestReset(email);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/reset")
    public ResponseEntity<ApiResponse<Void>> reset(
            @RequestParam String token,
            @RequestParam String password
    ) {
        String userId = service.validate(token);

        userService.changePassword(userId, password, password);

        service.delete(token);

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}