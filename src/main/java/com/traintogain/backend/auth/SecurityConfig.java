package com.traintogain.backend.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // ❌ Kein CSRF (JWT)
                .csrf(csrf -> csrf.disable())

                // ❌ Kein CORS (Postman)
                .cors(cors -> cors.disable())

                // ❌ Kein Session-State
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 🔐 Authorization
                .authorizeHttpRequests(auth -> auth

                        // 🔓 Auth-Endpunkte
                        .requestMatchers("/api/auth/**").permitAll()

                        // 🔓 H2 Console (DEV ONLY)
                        .requestMatchers("/h2-console/**").permitAll()

                        // 🔓 Preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 🔒 Alles andere geschützt
                        .anyRequest().authenticated()
                )

                // 🪟 H2 braucht Frames
                .headers(headers ->
                        headers.frameOptions(frame -> frame.disable())
                )

                // 🔑 JWT Filter
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
