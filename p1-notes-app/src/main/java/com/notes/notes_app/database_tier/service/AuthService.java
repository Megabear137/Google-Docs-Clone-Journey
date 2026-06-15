package com.notes.notes_app.database_tier.service;

import com.notes.notes_app.auth.JwtService;
import com.notes.notes_app.database_tier.dto.auth.AuthRequest;
import com.notes.notes_app.database_tier.dto.auth.AuthResponse;
import com.notes.notes_app.database_tier.entity.User;
import com.notes.notes_app.database_tier.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepo,
            JwtService jwtService,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(AuthRequest request) {
        String email = request.email();
        if(userRepo.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        String hashed = passwordEncoder.encode(request.password());

        User user = new User();
        user.setEmail(email);
        user.setCreatedAt(Instant.now());
        user.setPasswordHash(hashed);

        User saved = userRepo.save(user);
        String token = jwtService.issueToken(saved.getId());

        return new AuthResponse(token);
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepo.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        String passwordAttempt = request.password();
        if (!passwordEncoder.matches(passwordAttempt, user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        String token = jwtService.issueToken(user.getId());
        return new AuthResponse(token);

    }

}
