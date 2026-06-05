package com.notes.notes_app.repository;

import com.notes.notes_app.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByOwnerId(Long ownerId);

}
