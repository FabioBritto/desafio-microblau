package com.microblau.desafio.backend.controller.note.dto;


import com.microblau.desafio.backend.model.note.Note;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CreateNoteDTO(
        @Schema(description = "Site relacionado à nota", example = "Site exemplo - https://site.com")
        @NotBlank(message = "O endereço do site é obrigatório")
        @Size(max = 255, min = 10, message = "O endereço do site precisa ter ao menos 10 caracteres")
        String site,

        @Schema(description = "Equipamento relacionado à nota", example = "Equipamento exemplo")
        @NotBlank(message = "O nome do equipamento é obrigatório")
        @Size(max = 255, min = 5, message = "O nome do equipamento precisa ter ao menos 5 caracteres")
        String equipment,

        @Schema(description = "Variável relacionada à nota", example = "Variável exemplo")
        @NotBlank(message = "O nome da variável é obrigatória")
        @Size(max = 255, min = 3, message = "O nome da variável precisa ter ao menos 3 caracteres")
        String variable,

        @Schema(description = "Nome do autor da nota", example = "Autor da Silva Santos")
        @NotBlank(message = "O nome do(a) autor(a) é obrigatório(a)")
        @Size(max = 255, min = 5, message = "O nome do(a) autor(a) precisa ter ao menos 5 caracteres")
        String author,

        @Schema(description = "Mensagem da nota", example = "Lorem ipsum dolor sit amet consectetur adipiscing elit...")
        @NotEmpty(message = "A mensagem é obrigatória")
        String message
) {

        public static Note toEntity(CreateNoteDTO noteDTO) {
                Note note = new Note();
                note.setSite(noteDTO.site);
                note.setEquipment(noteDTO.equipment);
                note.setVariable(noteDTO.variable);
                note.setAuthor(noteDTO.author);
                note.setMessage(noteDTO.message);
                return note;
        }
}
