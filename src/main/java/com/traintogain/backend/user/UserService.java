package com.traintogain.backend.user;

import com.traintogain.backend.auth.refreshtoken.RefreshTokenService;
import com.traintogain.backend.exception.*;
import com.traintogain.backend.mail.MailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final RefreshTokenService refresh;
    private final MailService emailService;

    public UserService(
            UserRepository repo,
            PasswordEncoder encoder,
            RefreshTokenService refresh,
            MailService mailService
    ) {
        this.repo = repo;
        this.encoder = encoder;
        this.refresh = refresh;
        this.emailService = mailService;
    }

    private String normalize(String email) {
        return email.trim().toLowerCase();
    }

    public User register(String email, String username, String raw) {
        email = normalize(email);

        if (repo.findByEmail(email).isPresent())
            throw new EmailAlreadyExistsException("E-Mail wird bereits verwendet!");

        PasswordValidator.validate(raw);

        User u = new User();
        u.setEmail(email);
        u.setUsername(username);
        u.setPassword(encoder.encode(raw));

        String token = UUID.randomUUID().toString();
        u.setVerificationToken(token);
        u.setVerificationTokenExpiry(Instant.now().plusSeconds(86400));
        u.setEnabled(false);

        repo.save(u);

        emailService.sendVerificationEmail(u.getEmail(), token);

        return u;
    }

    public User login(String email, String raw) {
        email = normalize(email);

        User u = repo.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Ungültige Anmeldedaten!"));

        if (!u.isEnabled())
            throw new InvalidCredentialsException("E-Mail nicht bestätigt!");

        if (!encoder.matches(raw, u.getPassword()))
            throw new InvalidCredentialsException("Ungültige Anmeldedaten!");

        return u;
    }

    public boolean verifyEmail(String token) {
        User user = repo.findByVerificationToken(token)
                .orElseThrow(() -> new InvalidTokenException("Token ungültig"));

        if (user.getVerificationTokenExpiry() == null ||
                user.getVerificationTokenExpiry().isBefore(Instant.now())) {
            throw new InvalidTokenException("Token abgelaufen");
        }

        user.setEnabled(true);
        user.setVerificationToken(null);
        user.setVerificationTokenExpiry(null);

        repo.save(user);

        return true;
    }

    public User getById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Benutzer nicht gefunden!"));
    }

    public void changePassword(String id, String oldP, String newP) {
        User u = getById(id);

        if (!encoder.matches(oldP, u.getPassword()))
            throw new InvalidPasswordException("Altes Passwort ist falsch!");

        PasswordValidator.validate(newP);

        u.setPassword(encoder.encode(newP));
        repo.save(u);

        refresh.deleteTokensForUser(id);
    }

    public void forgotPassword(String email) {
        email = normalize(email);

        repo.findByEmail(email).ifPresent(user -> {
            String token = UUID.randomUUID().toString();

            user.setResetToken(token);
            user.setResetTokenExpiry(Instant.now().plusSeconds(900));

            repo.save(user);

            emailService.sendResetEmail(user.getEmail(), token);
        });
    }

    public void deleteById(String id) {
        if (!repo.existsById(id))
            throw new UserNotFoundException("Benutzer nicht gefunden!");

        repo.deleteById(id);

        refresh.deleteTokensForUser(id);
    }

    public User updateProfile(String id, String email, String username) {
        User u = getById(id);

        if (email != null && !email.isBlank()) {
            String normalized = normalize(email);

            User existing = repo.findByEmail(normalized).orElse(null);
            if (existing != null && !existing.getId().equals(id)) {
                throw new EmailAlreadyExistsException("E-Mail wird bereits verwendet!");
            }

            u.setEmail(normalized);
        }

        if (username != null && !username.isBlank())
            u.setUsername(username);

        return repo.save(u);
    }

    public void resetPassword(String token, String newPassword) {
        User user = repo.findByResetToken(token)
                .orElseThrow(() -> new InvalidTokenException("Token ungültig"));

        if (user.getResetTokenExpiry() == null ||
                user.getResetTokenExpiry().isBefore(Instant.now())) {
            throw new InvalidTokenException("Token abgelaufen");
        }

        PasswordValidator.validate(newPassword);

        user.setPassword(encoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        repo.save(user);

        refresh.deleteTokensForUser(user.getId());
    }
}