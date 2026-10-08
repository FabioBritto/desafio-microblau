package com.microblau.desafio.backend.controller.note.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CreateNoteDTO(
        @NotBlank(message = "O endereço do site é obrigatório")
        @Size(max = 255, min = 10, message = "O endereço do site precisa ter ao menos 5 caracteres")
        String site,

        @NotBlank(message = "O nome do equipamento é obrigatório")
        @Size(max = 255, min = 5, message = "O nome do equipamento precisa ter ao menos 5 caracteres")
        String equipment,

        @NotBlank(message = "O nome da variável é obrigatória")
        @Size(max = 255, min = 3, message = "O nome da variável precisa ter ao menos 3 caracteres")
        String variable,

        @NotBlank(message = "O nome do(a) autor(a) é obrigatório(a)")
        @Size(max = 255, min = 5, message = "O nome do(a) autor(a) precisa ter ao menos 5 caracteres")
        String author,

        @NotEmpty(message = "A mensagem é obrigatória")
        String message
) {


}
