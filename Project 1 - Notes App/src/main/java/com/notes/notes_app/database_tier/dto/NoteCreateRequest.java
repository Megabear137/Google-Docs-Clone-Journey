package com.notes.notes_app.database_tier.dto;

import jakarta.validation.constraints.NotBlank;

public record NoteCreateRequest(
        @NotBlank String title,
        @NotBlank String body) {

}