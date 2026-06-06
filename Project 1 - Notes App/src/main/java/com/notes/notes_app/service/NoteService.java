package com.notes.notes_app.service;

import com.notes.notes_app.dto.NoteCreateRequest;
import com.notes.notes_app.dto.NoteResponse;
import com.notes.notes_app.entity.Note;
import com.notes.notes_app.entity.User;
import com.notes.notes_app.repository.NoteRepository;
import com.notes.notes_app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NoteService(
            NoteRepository noteRepository,
            UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    public List<NoteResponse> list(Long owner_id) {
        return noteRepository
                .findByOwnerId(owner_id)
                .stream()
                .map(NoteResponse::from)
                .toList();
    }

    public NoteResponse get(Long owner_id, Long id) {
        Optional<Note> note = noteRepository.findById(id);
        if(note.isPresent() && note.get().getOwner().getId().equals(owner_id)) {
            return NoteResponse.from(note.get());
        }
        throw new RuntimeException("Note not Found");
    }

    public void delete(Long id, Long owner_id) {
        Optional<Note> note = noteRepository.findById(id);
        if(note.isPresent() && note.get().getOwner().getId().equals(owner_id)) {
            noteRepository.delete(note.get());
        }
    }

    public NoteResponse create(Long owner_id, NoteCreateRequest request) {
        User owner = userRepository.findById(owner_id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Note note = new Note();
        note.setOwner(owner);
        note.setTitle(request.title());
        note.setBody(request.body());
        note.setCreatedAt(Instant.now());

        Note saved = noteRepository.save(note);
        return NoteResponse.from(saved);
    }



}
