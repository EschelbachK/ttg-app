package com.traintogain.backend.user;

import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Setter
    @Indexed(unique = true)
    private String email;

    @Setter
    @Indexed(unique = true)
    private String username;

    @Setter
    private String password;

    @Setter
    private Role role = Role.USER;

    @Setter
    private String resetToken;

    @Setter
    private Instant resetTokenExpiry;

    @Setter
    private boolean enabled = false;

    @Setter
    private String verificationToken;

    @Setter
    private Instant verificationTokenExpiry;

    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }

    public String getResetToken() { return resetToken; }
    public Instant getResetTokenExpiry() { return resetTokenExpiry; }
    public boolean isEnabled() { return enabled; }

    public String getVerificationToken() { return verificationToken; }
    public Instant getVerificationTokenExpiry() { return verificationTokenExpiry; }

}
