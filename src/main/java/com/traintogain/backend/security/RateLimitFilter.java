package com.traintogain.backend.security;

import io.github.bucket4j.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends GenericFilter {

    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket resolveBucket(String key) {
        return buckets.computeIfAbsent(key, k ->
                Bucket.builder()
                        .addLimit(Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1))))
                        .build()
        );
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

        if (path.startsWith("/api/auth/login") || path.startsWith("/api/auth/forgot-password")) {
            String ip = req.getRemoteAddr();
            Bucket bucket = resolveBucket(ip);

            if (!bucket.tryConsume(1)) {
                res.setStatus(429);
                res.getWriter().write("Too many requests");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}