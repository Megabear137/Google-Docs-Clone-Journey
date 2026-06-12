package com.notes.notes_app.database_tier.dto;

import jakarta.validation.constraints.NotBlank;

public record NoteUpdateRequest(
        @NotBlank String title,
        @NotBlank String body) {

}
