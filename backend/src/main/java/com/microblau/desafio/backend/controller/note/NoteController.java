package com.microblau.desafio.backend.controller.note;

import com.microblau.desafio.backend.controller.note.dto.NoteDTO;
import com.microblau.desafio.backend.service.note.INoteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



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
}
