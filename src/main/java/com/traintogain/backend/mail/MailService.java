package com.traintogain.backend.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender sender;

    private final String from = "kai.eschelbach@gmx.net";

    private final String backendUrl;
    private final String appUrl;

    public MailService(JavaMailSender sender,
                       @Value("${backend.url:http://localhost:8080}") String backendUrl,
                       @Value("${app.url:ttg://}") String appUrl) {
        this.sender = sender;
        this.backendUrl = backendUrl;
        this.appUrl = appUrl;
    }

    public void send(String to, String subject, String text) {
        SimpleMailMessage m = new SimpleMailMessage();
        m.setFrom(from);
        m.setTo(to);
        m.setSubject(subject);
        m.setText(text);
        sender.send(m);
    }

    public void sendVerificationEmail(String to, String token) {
        String deepLink = appUrl + "verify?token=" + token;
        String fallback = backendUrl + "/api/auth/verify?token=" + token;

        send(
                to,
                "Account verifizieren!",
                "App öffnen:\n" + deepLink + "\n\nOder im Browser:\n" + fallback
        );
    }

    public void sendResetEmail(String to, String token) {
        String deepLink = appUrl + "reset-password?token=" + token;
        String fallback = backendUrl + "/api/auth/reset-password?token=" + token;

        send(
                to,
                "Passwort zurücksetzen!",
                "App öffnen:\n" + deepLink + "\n\nOder im Browser:\n" + fallback
        );
    }
}