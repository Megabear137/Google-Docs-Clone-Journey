package com.notes.notes_app.database_tier.dto.note;

import com.notes.notes_app.database_tier.dto.tag.TagResponse;
import com.notes.notes_app.database_tier.entity.Note;
import java.time.Instant;
import java.util.Set;

public record NoteResponse(
        Long id,
        String title,
        String body,
        Instant updatedAt,
        Set<TagResponse> tags) {

    public static NoteResponse from(Note note) {
        return new NoteResponse(
                note.getId(),
                note.getTitle(),
                note.getBody(),
                note.getUpdatedAt(),
                Set.copyOf(note.getTags().stream().map(TagResponse::from).toList())
        );
    }


}
