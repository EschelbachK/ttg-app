package com.traintogain.backend.passwordreset;

import com.traintogain.backend.user.User;
import com.traintogain.backend.user.UserRepository;
import com.traintogain.backend.auth.refreshtoken.RefreshTokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public PasswordResetService(
            PasswordResetTokenRepository tokenRepository,
            UserRepository userRepository,
            RefreshTokenService refreshTokenService
    ) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
    }

    // ✅ 1️⃣ RESET ANFORDERN (Mail)
    public void requestReset(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // alte Tokens löschen
        tokenRepository.deleteByUserId(user.getId());

        PasswordResetToken token = new PasswordResetToken(
                user.getId(),
                UUID.randomUUID().toString(),
                Instant.now().plusSeconds(60 * 30) // 30 Minuten
        );

        tokenRepository.save(token);

        // 📧 MAIL (später)
        System.out.println("RESET LINK:");
        System.out.println("https://frontend/reset?token=" + token.getToken());
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
