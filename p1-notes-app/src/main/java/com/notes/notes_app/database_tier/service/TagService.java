package com.notes.notes_app.database_tier.service;

import com.notes.notes_app.database_tier.dto.tag.TagCreateRequest;
import com.notes.notes_app.database_tier.dto.tag.TagResponse;
import com.notes.notes_app.database_tier.entity.Note;
import com.notes.notes_app.database_tier.entity.Tag;
import com.notes.notes_app.database_tier.entity.User;
import com.notes.notes_app.database_tier.exception.NoteNotFoundException;
import com.notes.notes_app.database_tier.exception.TagNotFoundException;
import com.notes.notes_app.database_tier.repository.NoteRepository;
import com.notes.notes_app.database_tier.repository.TagRepository;
import com.notes.notes_app.database_tier.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final NoteRepository noteRepository;

    public TagService(TagRepository tagRepository,
                      UserRepository userRepository,
                      NoteRepository noteRepository) {
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.noteRepository = noteRepository;
    }

    public TagResponse get(Long ownerId, String name) {
        Optional<Tag> tag = tagRepository.findByOwnerIdAndName(ownerId, name);

        if(tag.isPresent() && tag.get().getOwner().getId().equals(ownerId)) {
            return TagResponse.from(tag.get());
        }

        throw new TagNotFoundException(name);
    }

    public void delete(Long ownerId, String name) {
        Tag tag = tagRepository.findByOwnerIdAndName(ownerId, name)
                .orElseThrow( () -> new TagNotFoundException(name) );

        if (tag.getOwner().getId().equals(ownerId)) {
            tagRepository.delete(tag);
        } else {
            throw new TagNotFoundException(name);
        }
    }

    public TagResponse create(Long ownerId, TagCreateRequest request) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Tag> tagCheck = tagRepository.findByOwnerIdAndName(ownerId, request.name());

        if (tagCheck.isPresent()) {
            throw new RuntimeException("User already has tag by that name");
        }

        Tag tag = new Tag();
        tag.setOwner(owner);
        tag.setName(request.name());

        Tag saved = tagRepository.save(tag);
        return TagResponse.from(saved);
    }

    @Transactional
    public Set<TagResponse> addTagToNote(Long noteId, Long ownerId, String tagName) {
        Optional<Note> noteCheck = noteRepository.findById(noteId);

        if (noteCheck.isEmpty() || !noteCheck.get().getOwner().getId().equals(ownerId)) {
            throw new NoteNotFoundException(noteId);
        }

        Note note = noteCheck.get();

        Tag tag = tagRepository.findByOwnerIdAndName(ownerId, tagName)
        .orElseGet(() -> {
            Tag t = new Tag();
            t.setName(tagName);
            t.setOwner(note.getOwner());   // or fetch the User
            return tagRepository.save(t);
        });

        note.getTags().add(tag);
        Note saved = noteRepository.save(note);

        return Set.copyOf(saved.getTags()
                .stream()
                .map(TagResponse::from)
                .toList());
    }

    @Transactional
    public void removeTagFromNote(Long noteId, Long ownerId, String tagName) {
        Optional<Note> noteCheck = noteRepository.findById(noteId);

        if (noteCheck.isEmpty() || !noteCheck.get().getOwner().getId().equals(ownerId)) {
            throw new NoteNotFoundException(noteId);
        }

        Note note = noteCheck.get();

        Tag tag = tagRepository.findByOwnerIdAndName(ownerId, tagName)
                .orElseThrow( () -> new TagNotFoundException(tagName));

        note.getTags().remove(tag);
        Note saved = noteRepository.save(note);
    }

}
