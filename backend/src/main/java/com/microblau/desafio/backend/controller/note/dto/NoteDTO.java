package com.microblau.desafio.backend.controller.note.dto;

import java.sql.Timestamp;

public record NoteDTO(
        String site,
        String equipment,
        String variable,
        Timestamp timestamp,
        String author,
        String message
) {
}
