package com.notes.notes_app.database_tier.service;

import com.notes.notes_app.auth.JwtService;
import com.notes.notes_app.database_tier.dto.auth.AuthRequest;
import com.notes.notes_app.database_tier.dto.auth.AuthResponse;
import com.notes.notes_app.database_tier.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        return null;
    }

    public AuthResponse login(AuthRequest request) {
        return null;
    }

}
