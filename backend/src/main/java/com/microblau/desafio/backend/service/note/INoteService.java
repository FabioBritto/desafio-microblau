package com.microblau.desafio.backend.service.note;

import com.microblau.desafio.backend.controller.note.dto.CreateNoteDTO;
import com.microblau.desafio.backend.controller.note.dto.NoteDTO;
import com.microblau.desafio.backend.controller.note.dto.UpdateNoteDTO;
import com.microblau.desafio.backend.model.note.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

public interface INoteService {


    Page<NoteDTO> findAll(Pageable pageable, String site, String equipment, Instant startDate, Instant endDate);
    NoteDTO findById(String id);
    Long countNotes();
    NoteDTO create(CreateNoteDTO createNoteDTO);
    void createNotesWithList(List<Note> notes);
    NoteDTO update(String noteId, UpdateNoteDTO updateNoteDTO);
    void delete(String noteId);
}
