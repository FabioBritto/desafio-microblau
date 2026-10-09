package com.microblau.desafio.backend.controller.note.dto;

import com.microblau.desafio.backend.model.note.Note;
import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Timestamp;

public record NoteDTO(

        @Schema(description = "Identificador único da nota, gerado com UUID", example = "6492c8f7-2a93-455f-9117-8f4aff988ec8")
        String id,

        @Schema(description = "Site relacionado à nota", example = "Site exemplo - https://site.com")
        String site,

        @Schema(description = "Equipamento relacionado à nota", example = "Equipamento exemplo")
        String equipment,

        @Schema(description = "Variável relacionada à nota", example = "Variável exemplo")
        String variable,

        @Schema(description = "Data e hora de criação da nota", example = "2026-01-01T00:00:00Z")
        Timestamp timestamp,

        @Schema(description = "Nome do autor da nota", example = "Autor da Silva Santos")
        String author,

        @Schema(description = "Mensagem da nota", example = "Lorem ipsum dolor sit amet consectetur adipiscing elit...")
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
