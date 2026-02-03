package com.traintogain.backend.user;

import com.traintogain.backend.auth.refreshtoken.RefreshTokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    public UserService(UserRepository userRepository, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User register(String email, String username, String rawPassword) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already in use");
        }
        PasswordValidator.validate(rawPassword);
        String hashedPassword = passwordEncoder.encode(rawPassword);

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(hashedPassword);
        // role bleibt automatisch USER (Default in Entity)

        return userRepository.save(user);
    }

    public User login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return user;
    }

    public User getById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateProfile(
            String userId,
            String email,
            String username
    ) {
        User user = getById(userId);

        if (email != null && !email.isBlank()) {
            user.setEmail(email);
        }

        if (username != null && !username.isBlank()) {
            user.setUsername(username);
        }

        return userRepository.save(user);
    }

    public void changePassword(
            String userId,
            String oldPassword,
            String newPassword
    ) {
        User user = getById(userId);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        PasswordValidator.validate(newPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        refreshTokenService.deleteTokensForUser(userId);

    }

    public void deleteById(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(userId);
    }
}
