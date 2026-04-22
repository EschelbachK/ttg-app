package com.traintogain.backend.user;

import com.traintogain.backend.auth.refreshtoken.RefreshTokenService;
import com.traintogain.backend.exception.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final RefreshTokenService refresh;

    public UserService(UserRepository repo, PasswordEncoder encoder, RefreshTokenService refresh) {
        this.repo = repo;
        this.encoder = encoder;
        this.refresh = refresh;
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

        return repo.save(u);
    }

    public User login(String email, String raw) {
        email = normalize(email);

        User u = repo.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Ungültige Anmeldedaten!"));

        if (!encoder.matches(raw, u.getPassword()))
            throw new InvalidCredentialsException("Ungültige Anmeldedaten!");

        return u;
    }

    public User getById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Benutzer nicht gefunden!"));
    }

    public User updateProfile(String id, String email, String username) {
        User u = getById(id);

        if (email != null && !email.isBlank()) {
            String normalized = normalize(email);

            repo.findByEmail(normalized).ifPresent(e -> {
                if (!e.getId().equals(id))
                    throw new EmailAlreadyExistsException("E-Mail wird bereits verwendet!");
            });

            u.setEmail(normalized);
        }

        if (username != null && !username.isBlank())
            u.setUsername(username);

        return repo.save(u);
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

    public void deleteById(String id) {
        if (!repo.existsById(id))
            throw new UserNotFoundException("Benutzer nicht gefunden!");

        repo.deleteById(id);
    }
}