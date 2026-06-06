package com.notes.notes_app.dto;

import jakarta.validation.constraints.NotBlank;

public record NoteUpdateRequest(
        @NotBlank String title,
        @NotBlank String body) {

}
