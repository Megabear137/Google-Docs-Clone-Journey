package com.notes.notes_app.database_tier.controller;

import com.notes.notes_app.database_tier.dto.NoteCreateRequest;
import com.notes.notes_app.database_tier.dto.NoteResponse;
import com.notes.notes_app.database_tier.dto.NoteUpdateRequest;
import com.notes.notes_app.database_tier.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService service;

    public NoteController(NoteService service) {
        this.service = service;
    }

    @GetMapping
    public List<NoteResponse> list() {
        Long ownerId = 1L; // TODO: replace with authenticated
        return service.list(ownerId);
    }

    @GetMapping("/{id}")
    public NoteResponse get(@PathVariable Long id) {
        Long ownerId = 1L; // TODO: replace with authenticated
        return service.get(ownerId, id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteResponse create(@Valid @RequestBody NoteCreateRequest request) {
        Long ownerId = 1L; // TODO: replace with authenticated
        return service.create(ownerId, request);
    }

    @PutMapping("/{id}")
    public NoteResponse update(@Valid @RequestBody NoteUpdateRequest request, @PathVariable Long id){
        Long ownerId = 1L; // TODO: replace with authenticated
        return service.update(ownerId, id, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Long ownerId = 1L; // TODO: replace with authenticated
        service.delete(ownerId, id);
    }

}
