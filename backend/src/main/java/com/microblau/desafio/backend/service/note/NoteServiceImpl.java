package com.microblau.desafio.backend.service.note;

import com.microblau.desafio.backend.controller.note.dto.CreateNoteDTO;
import com.microblau.desafio.backend.controller.note.dto.NoteDTO;
import com.microblau.desafio.backend.controller.note.dto.UpdateNoteDTO;
import com.microblau.desafio.backend.model.note.Note;
import com.microblau.desafio.backend.repository.NoteRepository;
import com.microblau.desafio.backend.util.exceptions.NoteNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
        Note note = CreateNoteDTO.toEntity(createNoteDTO);
        note.setId(UUID.randomUUID().toString());

        Note created = noteRepository.save(note);
        return NoteDTO.fromEntity(created);
    }

    @Override
    public NoteDTO update(String noteId, UpdateNoteDTO updateNoteDTO) {
        Note existingNote = noteRepository.findById(noteId).orElseThrow(NoteNotFoundException::new);
        existingNote.setSite(updateNoteDTO.site());
        existingNote.setEquipment(updateNoteDTO.equipment());
        existingNote.setVariable(updateNoteDTO.variable());
        existingNote.setMessage(updateNoteDTO.message());

        Note updatedNote = noteRepository.save(existingNote);
        return NoteDTO.fromEntity(updatedNote);
    }
}
