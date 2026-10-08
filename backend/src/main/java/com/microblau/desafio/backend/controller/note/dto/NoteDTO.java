package com.microblau.desafio.backend.controller.note.dto;

import com.microblau.desafio.backend.model.note.Note;

import java.sql.Timestamp;

public record NoteDTO(
        String id,
        String site,
        String equipment,
        String variable,
        Timestamp timestamp,
        String author,
        String message
) {

    public static NoteDTO fromEntity(Note note) {
        return new NoteDTO(
                note.getId(),
                note.getSite(),
                note.getEquipment(),
                note.getVariable(),
                note.getTimestamp(),
                note.getAuthor(),
                note.getMessage()
        );
    }


}
