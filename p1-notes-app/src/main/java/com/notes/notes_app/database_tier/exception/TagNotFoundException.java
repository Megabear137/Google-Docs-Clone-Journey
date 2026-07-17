package com.notes.notes_app.database_tier.exception;

public class TagNotFoundException extends RuntimeException {

    public TagNotFoundException(String name) {
        super("Tag not found with name: " + name);
    }
}
