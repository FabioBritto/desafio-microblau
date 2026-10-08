package com.microblau.desafio.backend.controller.note;

import com.microblau.desafio.backend.controller.note.dto.CreateNoteDTO;
import com.microblau.desafio.backend.controller.note.dto.NoteDTO;
import com.microblau.desafio.backend.controller.note.dto.UpdateNoteDTO;
import com.microblau.desafio.backend.service.note.INoteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequestMapping("/api/v1/notes")
public class NoteController {

    private final INoteService noteService;

    public NoteController(INoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping()
    public ResponseEntity<Page<NoteDTO>> findAll(@RequestParam(required = false) String site,
                                                 @RequestParam(required = false) String equipment,
                                                 @RequestParam(required = false) String startDate,
                                                 @RequestParam(required = false) String endDate,
                                                 Pageable pageable
                                              ) {
        return ResponseEntity.status(HttpStatus.OK).body(noteService.findAll(pageable, site, equipment, startDate, endDate));
    }

    @PostMapping()
    public ResponseEntity<NoteDTO> create(@Valid @RequestBody CreateNoteDTO createNoteDTO) {
        NoteDTO createdNote = noteService.create(createNoteDTO);
        URI uri = URI.create("/api/v1/notes" + createdNote.id());
        return ResponseEntity.created(uri).body(createdNote);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteDTO> update(@PathVariable String id, @Valid @RequestBody UpdateNoteDTO updateNoteDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(noteService.update(id, updateNoteDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
