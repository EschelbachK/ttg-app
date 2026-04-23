package com.traintogain.backend.user;

import java.util.regex.Pattern;

public class UsernameValidator {

    private static final Pattern VALID =
            Pattern.compile("^[a-zA-Z0-9_]{3,20}$");

    public static void validate(String username) {
        if (username == null || !VALID.matcher(username).matches()) {
            throw new RuntimeException(
                    "Username must be 3-20 characters (letters, numbers, underscore)"
            );
        }
    }
}