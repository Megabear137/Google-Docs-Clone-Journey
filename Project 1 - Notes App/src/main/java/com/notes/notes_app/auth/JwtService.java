package com.notes.notes_app.auth;

import com.notes.notes_app.auth.config.JwtProperties;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {

    private final JwtEncoder encoder;
    private final long expirationMs;

    public JwtService(JwtEncoder encoder, JwtProperties props) {
        this.encoder = encoder;
        this.expirationMs = props.expirationMs();
    }

    public String issueToken(Long userId) {

        Instant now = Instant.now();
        Instant exp = now.plusMillis(expirationMs);

        JwsAlgorithm alg = MacAlgorithm.HS256;

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(userId.toString())
                .issuedAt(now)
                .expiresAt(exp)
                .build();

        JwsHeader header = JwsHeader.with(alg).build();

        JwtEncoderParameters params = JwtEncoderParameters.from(
                header,
                claims
        );

        return encoder.encode(params).getTokenValue();
    }
}
