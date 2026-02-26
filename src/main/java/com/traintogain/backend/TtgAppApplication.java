package com.traintogain.backend;

import com.traintogain.backend.user.User;
import com.traintogain.backend.user.UserRepository;
import com.traintogain.backend.user.Role;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TtgAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(TtgAppApplication.class, args);
    }

    // 🔥 TEST USER INITIALIZER
    @Bean
    CommandLineRunner initUser(UserRepository repo, PasswordEncoder encoder) {
        return args -> {

            if (repo.findByEmail("test@test.de").isEmpty()) {

                User user = new User();
                user.setEmail("test@test.de");
                user.setUsername("testuser");
                user.setPassword(encoder.encode("123456"));
                user.setRole(Role.USER);

                repo.save(user);

                System.out.println(">>> TEST USER CREATED <<<");
            }
        };
    }
}