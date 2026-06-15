package com.notes.notes_app.database_tier.controller;

import com.notes.notes_app.database_tier.dto.auth.AuthRequest;
import com.notes.notes_app.database_tier.dto.auth.AuthResponse;
import com.notes.notes_app.database_tier.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody AuthRequest request) {
        return service.register(request);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {
        return service.login(request);
    }

}
