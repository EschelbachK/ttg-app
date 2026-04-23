package com.traintogain.backend.mail.verification;

import com.traintogain.backend.mail.MailService;
import com.traintogain.backend.user.User;
import com.traintogain.backend.user.UserRepository;
import com.traintogain.backend.exception.InvalidTokenException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class VerificationService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;
    private final MailService mailService;

    public VerificationService(VerificationTokenRepository verificationTokenRepository,
                               UserRepository userRepository,
                               MailService mailService) {
        this.verificationTokenRepository = verificationTokenRepository;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    public String createToken(User user) {
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUserId(user.getId());
        verificationToken.setToken(token);
        verificationToken.setExpiryDate(Instant.now().plus(15, ChronoUnit.MINUTES));

        verificationTokenRepository.save(verificationToken);

        return token;
    }

    public void sendVerificationMail(User user) {
        String token = createToken(user);
        String link = "http://localhost:8080/api/auth/verify?token=" + token;

        mailService.send(
                user.getEmail(),
                "Verify your account",
                "Click here: " + link
        );
    }

    public void verify(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Token ungültig"));

        if (verificationToken.getExpiryDate().isBefore(Instant.now())) {
            throw new InvalidTokenException("Token abgelaufen");
        }

        User user = userRepository.findById(verificationToken.getUserId())
                .orElseThrow(() -> new InvalidTokenException("User nicht gefunden"));

        user.setEnabled(true);
        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);
    }

}
