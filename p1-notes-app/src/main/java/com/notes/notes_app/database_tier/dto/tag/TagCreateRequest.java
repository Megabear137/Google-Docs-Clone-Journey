package com.notes.notes_app.database_tier.dto.tag;

import jakarta.validation.constraints.NotBlank;

public record TagCreateRequest(
        @NotBlank String name) {
}
