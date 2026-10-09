package com.microblau.desafio.backend.service.note;

import com.microblau.desafio.backend.controller.note.dto.CreateNoteDTO;
import com.microblau.desafio.backend.controller.note.dto.NoteDTO;
import com.microblau.desafio.backend.controller.note.dto.UpdateNoteDTO;
import com.microblau.desafio.backend.model.note.Note;
import com.microblau.desafio.backend.repository.NoteRepository;
import com.microblau.desafio.backend.util.exceptions.InvalidDateException;
import com.microblau.desafio.backend.util.exceptions.NoteNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@Service
public class NoteServiceImpl implements INoteService {

    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public Page<NoteDTO> findAll(Pageable pageable, String site, String equipment, Instant startDate, Instant endDate){

        if(startDate != null && endDate != null) {
            if(startDate.isAfter(endDate)) throw new InvalidDateException("A data informada não é válida");
        }

        return noteRepository.findAll(blankStringToNull(site), blankStringToNull(equipment), fromInstantToTimestamp(startDate), fromInstantToTimestamp(endDate), pageable).map(NoteDTO::fromEntity);
    }

    @Override
    public NoteDTO findById(String id) {
        Note existingNote = noteRepository.findById(id).orElseThrow(NoteNotFoundException::new);

        return NoteDTO.fromEntity(existingNote);
    }

    @Override
    public Long countNotes() {
        return noteRepository.count();
    }

    @Override
    public NoteDTO create(CreateNoteDTO createNoteDTO) {
        Note note = CreateNoteDTO.toEntity(createNoteDTO);
        note.setId(UUID.randomUUID().toString());

        Note created = noteRepository.save(note);
        return NoteDTO.fromEntity(created);
    }

    @Override
    public void createNotesWithList(List<Note> notes) {
        noteRepository.saveAll(notes);
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

    @Override
    @Transactional
    public void delete(String noteId) {
        if(!noteRepository.existsById(noteId)) throw new NoteNotFoundException("Nota não encontrada a partir deste ID");
        noteRepository.deleteById(noteId);
    }


    private Timestamp fromInstantToTimestamp(Instant date) {
        return date == null ? null : Timestamp.from(date);
    }

    private String blankStringToNull(String field) {
        if(field == null || field.isBlank()) return null;
        return field;
    }
}
