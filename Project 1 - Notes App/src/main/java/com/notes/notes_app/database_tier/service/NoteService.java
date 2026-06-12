package com.notes.notes_app.database_tier.service;

import com.notes.notes_app.database_tier.dto.NoteCreateRequest;
import com.notes.notes_app.database_tier.dto.NoteResponse;
import com.notes.notes_app.database_tier.dto.NoteUpdateRequest;
import com.notes.notes_app.database_tier.entity.Note;
import com.notes.notes_app.database_tier.entity.User;
import com.notes.notes_app.database_tier.exception.NoteNotFoundException;
import com.notes.notes_app.database_tier.repository.NoteRepository;
import com.notes.notes_app.database_tier.repository.UserRepository;
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

    public List<NoteResponse> list(Long ownerId) {
        return noteRepository
                .findByOwnerId(ownerId)
                .stream()
                .map(NoteResponse::from)
                .toList();
    }

    public NoteResponse get(Long ownerId, Long id) {
        Optional<Note> note = noteRepository.findById(id);
        if(note.isPresent() && note.get().getOwner().getId().equals(ownerId)) {
            return NoteResponse.from(note.get());
        }
        throw new NoteNotFoundException(id);
    }

    public void delete(Long ownerId, Long id) {
        Optional<Note> note = noteRepository.findById(id);
        if(note.isPresent() && note.get().getOwner().getId().equals(ownerId)) {
            noteRepository.delete(note.get());
        }
        else throw new NoteNotFoundException(id);
    }

    public NoteResponse update(Long ownerId, Long id, NoteUpdateRequest request) {
        Optional<Note> note = noteRepository.findById(id);
        if(note.isPresent() && note.get().getOwner().getId().equals(ownerId)) {
            Note foundNote = note.get();

            foundNote.setTitle(request.title());
            foundNote.setBody(request.body());
            foundNote.setUpdatedAt(Instant.now());

            Note saved = noteRepository.save(foundNote);
            return NoteResponse.from(saved);
        }
        else throw new NoteNotFoundException(id);
    }

    public NoteResponse create(Long ownerId, NoteCreateRequest request) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Note note = new Note();
        note.setOwner(owner);
        note.setTitle(request.title());
        note.setBody(request.body());
        note.setCreatedAt(Instant.now());
        note.setUpdatedAt(Instant.now());

        Note saved = noteRepository.save(note);
        return NoteResponse.from(saved);
    }



}
