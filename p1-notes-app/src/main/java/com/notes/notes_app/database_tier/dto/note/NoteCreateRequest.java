package com.notes.notes_app.database_tier.dto.note;

import jakarta.validation.constraints.NotBlank;

public record NoteCreateRequest(
        @NotBlank String title,
        String body) {
}