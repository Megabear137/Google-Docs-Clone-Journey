package com.notes.notes_app.database_tier.dto.tag;

import com.notes.notes_app.database_tier.entity.Tag;

public record TagResponse(Long id, String name) {

    public static TagResponse from(Tag tag) {
        return new TagResponse(
                tag.getId(),
                tag.getName()
        );
    }

}
