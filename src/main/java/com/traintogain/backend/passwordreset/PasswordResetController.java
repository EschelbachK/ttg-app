package com.traintogain.backend.passwordreset;

import com.traintogain.backend.passwordreset.dto.ConfirmPasswordResetRequest;
import com.traintogain.backend.passwordreset.dto.PasswordResetRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/password-reset")
public class PasswordResetController {

    private final PasswordResetService service;

    public PasswordResetController(PasswordResetService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> requestReset(
            @RequestBody PasswordResetRequest request
    ) {
        service.requestReset(request.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm")
    public ResponseEntity<Void> confirmReset(
            @RequestBody ConfirmPasswordResetRequest request
    ) {
        service.resetPassword(
                request.token(),
                request.newPassword()
        );
        return ResponseEntity.ok().build();
    }
}
