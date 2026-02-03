package com.traintogain.backend.user;

import java.util.regex.Pattern;

public class PasswordValidator {

    // min 8 Zeichen, 1 Großbuchstabe, 1 Zahl
    private static final Pattern STRONG_PASSWORD =
            Pattern.compile("^(?=.*[A-Z])(?=.*\\d).{8,}$");

    public static void validate(String password) {
        if (!STRONG_PASSWORD.matcher(password).matches()) {
            throw new RuntimeException(
                    "Password must be at least 8 characters long and contain one uppercase letter and one number"
            );
        }
    }
}
