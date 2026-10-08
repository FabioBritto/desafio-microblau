package com.microblau.desafio.backend.service;

import com.microblau.desafio.backend.controller.note.dto.CreateNoteDTO;
import com.microblau.desafio.backend.controller.note.dto.NoteDTO;
import com.microblau.desafio.backend.controller.note.dto.UpdateNoteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface INoteService {


    Page<NoteDTO> findAll(Pageable pageable);
    NoteDTO create(CreateNoteDTO createNoteDTO);
    NoteDTO update(UpdateNoteDTO updateNoteDTO);
}
