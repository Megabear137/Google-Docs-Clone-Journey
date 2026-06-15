package com.notes.notes_app.database_tier.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest (
        @NotBlank String email,
        @NotBlank String password) {
}
