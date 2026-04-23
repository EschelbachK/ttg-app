package com.traintogain.backend.mail;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController {

    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping("/mail-test")
    public String sendTestMail() {
        mailService.send(
                "kai.eschelbach@gmx.net",
                "TTG Test Mail",
                "Email funktioniert"
        );
        return "mail sent";
    }
}