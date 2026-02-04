package com.traintogain.backend.mail;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class MailTestRunner implements CommandLineRunner {

    private final MailService mailService;

    public MailTestRunner(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public void run(String... args) {
        mailService.send(
                "test@example.com",
                "Mail test successful",
                "If you can read this, mail sending works 🎉"
        );
    }
}