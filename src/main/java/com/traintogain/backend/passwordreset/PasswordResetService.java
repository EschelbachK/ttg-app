package com.traintogain.backend.passwordreset;

import com.traintogain.backend.auth.refreshtoken.RefreshTokenService;
import com.traintogain.backend.mail.MailService;
import com.traintogain.backend.user.User;
import com.traintogain.backend.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final MailService mailService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${app.frontend.reset-url}")
    private String frontendResetUrl;

    public PasswordResetService(
            PasswordResetTokenRepository tokenRepository,
            UserRepository userRepository,
            RefreshTokenService refreshTokenService,
            MailService mailService
    ) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
        this.mailService = mailService;
    }

    // ✅ 1️⃣ RESET ANFORDERN (MAIL)
    public void requestReset(String email) {

        userRepository.findByEmail(email).ifPresent(user -> {

            // alte Tokens löschen
            tokenRepository.deleteByUserId(user.getId());

            PasswordResetToken token = new PasswordResetToken(
                    user.getId(),
                    UUID.randomUUID().toString(),
                    Instant.now().plusSeconds(60 * 30) // 30 Minuten
            );

            tokenRepository.save(token);

            String resetLink = frontendResetUrl + "?token=" + token.getToken();

            mailService.send(
                    user.getEmail(),
                    "Reset your password",
                    """
                    You requested a password reset.

                    Click the link below to reset your password:
                    %s

                    If you did not request this, you can ignore this email.
                    """.formatted(resetLink)
            );
        });

    }

    // ✅ 2️⃣ PASSWORT ZURÜCKSETZEN
    public void resetPassword(String token, String newPassword) {

        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid reset token"));

        if (resetToken.isExpired()) {
            tokenRepository.delete(resetToken);
            throw new RuntimeException("Reset token expired");
        }

        User user = userRepository.findById(resetToken.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // 🔥 SECURITY: alle Sessions killen
        refreshTokenService.deleteTokensForUser(user.getId());

        tokenRepository.delete(resetToken);
    }
}
