package com.notes.notes_app.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.nio.charset.StandardCharsets;

@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(String secret, long expirationMs) {
    public JwtProperties{
        if ( secret.isBlank() ||
                secret.charAt(0) == '$' ||
                secret.getBytes(StandardCharsets.UTF_8).length < 32
        ) {
            throw new IllegalStateException("secret is invalid");
        }
    }
}