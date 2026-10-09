package com.microblau.desafio.backend.controller.note;

import com.microblau.desafio.backend.controller.note.dto.CreateNoteDTO;
import com.microblau.desafio.backend.controller.note.dto.NoteDTO;
import com.microblau.desafio.backend.controller.note.dto.UpdateNoteDTO;
import com.microblau.desafio.backend.service.note.INoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;


@RestController
@RequestMapping("/api/v1/notes")
@Tag(name = "Notas")
public class NoteController {

    private final INoteService noteService;

    public NoteController(INoteService noteService) {
        this.noteService = noteService;
    }

    @Operation(
            summary = "Recupera a lista paginada de todas as notas",
            description = "Permite filtrar por site, equipamento e/ou intervalo de datas (início e fim)."
    )
    @GetMapping
    public ResponseEntity<Page<NoteDTO>> findAll(
            @Parameter(description = "Nome do site para filtrar as notas", example = "Site-SP01")
            @RequestParam(required = false) String site,

            @Parameter(description = "Nome ou código do equipamento para filtrar as notas", example = "EQ-1234")
            @RequestParam(required = false) String equipment,

            @Parameter(description = "Data/hora inicial do período (ISO 8601, UTC)", example = "2026-01-01T00:00:00Z")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,

            @Parameter(description = "Data/hora final do período (ISO 8601, UTC)", example = "2026-12-31T23:59:59Z")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate,

            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(noteService.findAll(pageable, site, equipment, startDate, endDate));
    }

    @Operation(summary = "Recupera uma nota pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Nota encontrada"),
            @ApiResponse(responseCode = "404", description = "Nota não encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<NoteDTO> findById(
            @Parameter(description = "ID da nota", example = "6492c8f7-2a93-455f-9117-8f4aff988ec8")
            @PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(noteService.findById(id));
    }

    @Operation(summary = "Cria uma nova nota")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Nota criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<NoteDTO> create(@Valid @RequestBody CreateNoteDTO createNoteDTO) {
        NoteDTO createdNote = noteService.create(createNoteDTO);
        URI uri = URI.create("/api/v1/notes/" + createdNote.id());
        return ResponseEntity.created(uri).body(createdNote);
    }

    @Operation(summary = "Atualiza uma nota existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Nota atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Nota não encontrada", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<NoteDTO> update(
            @Parameter(description = "ID da nota a ser atualizada", example = "6492c8f7-2a93-455f-9117-8f4aff988ec8")
            @PathVariable String id,
            @Valid @RequestBody UpdateNoteDTO updateNoteDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(noteService.update(id, updateNoteDTO));
    }

    @Operation(summary = "Remove uma nota pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Nota removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nota não encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID da nota a ser removida", example = "6492c8f7-2a93-455f-9117-8f4aff988ec8")
            @PathVariable String id) {
        noteService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
