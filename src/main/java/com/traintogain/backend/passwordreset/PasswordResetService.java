package com.traintogain.backend.passwordreset;

import com.traintogain.backend.mail.MailService;
import com.traintogain.backend.passwordreset.PasswordResetRepository;
import com.traintogain.backend.user.User;
import com.traintogain.backend.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class PasswordResetService {

    private static final long EXP = 60 * 30;

    private final PasswordResetRepository repo;
    private final UserRepository userRepo;
    private final MailService mail;

    public PasswordResetService(PasswordResetRepository repo,
                                UserRepository userRepo,
                                MailService mail) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.mail = mail;
    }

    public void requestReset(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));

        repo.deleteByUserId(user.getId());

        String token = UUID.randomUUID().toString();

        repo.save(new com.traintogain.backend.auth.passwordreset.PasswordResetToken(
                user.getId(),
                token,
                Instant.now().plusSeconds(EXP)
        ));

        String link = "http://localhost:3000/reset-password?token=" + token;

        mail.send(
                user.getEmail(),
                "Passwort zurücksetzen",
                "Klicke auf den Link um dein Passwort zurückzusetzen:\n" + link
        );
    }

    public String validate(String token) {
        com.traintogain.backend.auth.passwordreset.PasswordResetToken t = repo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token ungültig"));

        if (t.isExpired()) {
            repo.delete(t);
            throw new RuntimeException("Token abgelaufen");
        }

        return t.getUserId();
    }

    public void delete(String token) {
        repo.findByToken(token).ifPresent(repo::delete);
    }
}