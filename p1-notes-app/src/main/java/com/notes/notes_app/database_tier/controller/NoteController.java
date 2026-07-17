package com.notes.notes_app.database_tier.controller;

import com.notes.notes_app.database_tier.dto.note.NoteCreateRequest;
import com.notes.notes_app.database_tier.dto.note.NoteResponse;
import com.notes.notes_app.database_tier.dto.note.NoteUpdateRequest;
import com.notes.notes_app.database_tier.dto.tag.TagCreateRequest;
import com.notes.notes_app.database_tier.dto.tag.TagResponse;
import com.notes.notes_app.database_tier.service.NoteService;
import com.notes.notes_app.database_tier.service.TagService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;
    private final TagService tagService;

    public NoteController(NoteService noteService,
                          TagService tagService) {
        this.noteService = noteService;
        this.tagService = tagService;
    }

    @GetMapping
    public List<NoteResponse> list(@RequestParam(required = false) String search,
                                   @AuthenticationPrincipal Jwt jwt) {
        Long ownerId = Long.parseLong(jwt.getSubject());
        return noteService.list(ownerId, search);
    }

    @GetMapping("/{id}")
    public NoteResponse get(@PathVariable Long id,
                            @AuthenticationPrincipal Jwt jwt) {
        Long ownerId = Long.parseLong(jwt.getSubject());
        return noteService.get(ownerId, id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteResponse create(@Valid @RequestBody NoteCreateRequest request,
                               @AuthenticationPrincipal Jwt jwt) {
        Long ownerId = Long.parseLong(jwt.getSubject());
        return noteService.create(ownerId, request);
    }

    @PutMapping("/{id}")
    public NoteResponse update(@Valid @RequestBody NoteUpdateRequest request,
                               @PathVariable Long id,
                               @AuthenticationPrincipal Jwt jwt){
        Long ownerId = Long.parseLong(jwt.getSubject());
        return noteService.update(ownerId, id, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id,
                       @AuthenticationPrincipal Jwt jwt) {
        Long ownerId = Long.parseLong(jwt.getSubject());
        noteService.delete(ownerId, id);
    }

    @PostMapping("/{noteId}/tags")
    @ResponseStatus(HttpStatus.OK)
    public Set<TagResponse> addTagToNote(@PathVariable Long noteId,
                                         @Valid @RequestBody TagCreateRequest request,
                                         @AuthenticationPrincipal Jwt jwt) {
        Long ownerId = Long.parseLong(jwt.getSubject());
        return tagService.addTagToNote(
                noteId,
                ownerId,
                request.name());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{noteId}/tags/{name}")
    public void deleteTagFromNote(@PathVariable String name,
                                  @PathVariable Long noteId,
                                  @AuthenticationPrincipal Jwt jwt) {
        Long ownerId = Long.parseLong(jwt.getSubject());
        tagService.removeTagFromNote(
                noteId,
                ownerId,
                name);
    }

}
