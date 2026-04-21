package com.traintogain.backend.auth;

import com.traintogain.backend.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final SecretKey key;
    private final long exp = 1000L * 60 * 15;

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User u) {
        return Jwts.builder()
                .setClaims(Map.of(
                        "role", u.getRole().name(),
                        "username", u.getUsername()
                ))
                .setSubject(u.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + exp))
                .signWith(key)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            extract(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUserId(String t) { return extract(t).getSubject(); }
    public String extractRole(String t) { return extract(t).get("role", String.class); }
    public String extractUsername(String t) { return extract(t).get("username", String.class); }

    private Claims extract(String t) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(t).getBody();
    }
}