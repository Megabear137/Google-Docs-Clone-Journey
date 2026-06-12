package com.notes.notes_app.database_tier.dto;

import com.notes.notes_app.database_tier.entity.Note;
import java.time.Instant;

public record NoteResponse(Long id, String title, String body, Instant updatedAt) {

    public static NoteResponse from(Note note) {
        return new NoteResponse(
                note.getId(),
                note.getTitle(),
                note.getBody(),
                note.getUpdatedAt()
        );
    }


}
