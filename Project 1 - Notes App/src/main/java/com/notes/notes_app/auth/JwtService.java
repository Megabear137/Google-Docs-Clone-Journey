package com.notes.notes_app.auth;

import com.notes.notes_app.auth.config.JwtProperties;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final JwtEncoder encoder;
    private final long expirationMs;

    public JwtService(JwtEncoder encoder, JwtProperties props) {
        this.encoder = encoder;
        this.expirationMs = props.expirationMs();
    }

    public String issueToken(Long userId) {
        return ""; //TODO
    }
}
