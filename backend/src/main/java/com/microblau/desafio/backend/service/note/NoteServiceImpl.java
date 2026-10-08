package com.microblau.desafio.backend.service;

import com.microblau.desafio.backend.controller.note.dto.CreateNoteDTO;
import com.microblau.desafio.backend.controller.note.dto.NoteDTO;
import com.microblau.desafio.backend.controller.note.dto.UpdateNoteDTO;
import com.microblau.desafio.backend.repository.NoteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NoteServiceImpl implements INoteService {

    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public Page<NoteDTO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public NoteDTO create(CreateNoteDTO createNoteDTO) {
        return null;
    }

    @Override
    public NoteDTO update(UpdateNoteDTO updateNoteDTO) {
        return null;
    }
}
