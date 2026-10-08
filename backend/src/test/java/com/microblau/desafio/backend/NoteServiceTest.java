package com.microblau.desafio.backend;

import com.microblau.desafio.backend.controller.note.dto.CreateNoteDTO;
import com.microblau.desafio.backend.controller.note.dto.NoteDTO;
import com.microblau.desafio.backend.model.note.Note;
import com.microblau.desafio.backend.repository.NoteRepository;
import com.microblau.desafio.backend.service.note.INoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.time.Instant;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Captor
    private ArgumentCaptor<Note> noteCaptor;

    @InjectMocks
    private INoteService noteService;

    private CreateNoteDTO payloadRequest;

    @BeforeEach
    void setup() {
        payloadRequest = new CreateNoteDTO(
                "Exemplo de site",
                "Exemplo de Equipamento",
                "Exemplo de variável",
                "Exemplo de Autor",
                "Exemplo de mensagem"
        );
    }

    @Test
    @DisplayName("Deveria montar um Note com os dados do DTO e gerar um ID")
    void create_shouldMapFieldsAndGenerateId() {
        when(noteRepository.save(any(Note.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        noteService.create(payloadRequest);

        verify(noteRepository).save(noteCaptor.capture());
        Note noteToCreate = noteCaptor.getValue();

        assertThat(noteToCreate.getId()).isNotBlank();
        assertThat(noteToCreate.getSite()).isEqualTo(payloadRequest.site());
        assertThat(noteToCreate.getEquipment()).isEqualTo(payloadRequest.equipment());
        assertThat(noteToCreate.getVariable()).isEqualTo(payloadRequest.variable());
        assertThat(noteToCreate.getAuthor()).isEqualTo(payloadRequest.author());
        assertThat(noteToCreate.getMessage()).isEqualTo(payloadRequest.message());
    }

    @Test
    @DisplayName("Deveria retornar um NoteDTO a partir de uma entidade criada")
    void create_shouldReturnDtoFromCreatedNote() {
        Timestamp now = Timestamp.from(Instant.now());

        when(noteRepository.save(any(Note.class))).thenAnswer(invocation -> {
            Note note = invocation.getArgument(0);
            note.setTimestamp(now);
            return note;
        });

        NoteDTO createdNote = noteService.create(payloadRequest);

        assertThat(createdNote.id()).isNotBlank();
        assertThat(createdNote.site()).isEqualTo(payloadRequest.site());
        assertThat(createdNote.equipment()).isEqualTo(payloadRequest.equipment());
        assertThat(createdNote.variable()).isEqualTo(payloadRequest.variable());
        assertThat(createdNote.author()).isEqualTo(payloadRequest.author());
        assertThat(createdNote.message()).isEqualTo(payloadRequest.message());
        assertThat(createdNote.timestamp()).isEqualTo(now);
    }

    @Test
    @DisplayName("Deve lançar exceção por erro ao criar")
    void create_shouldThrowException() {
        when(noteRepository.save(any(Note.class)))
                .thenThrow(new RuntimeException("Não foi possível cadastrar uma Nota"));

        assertThatThrownBy(() -> noteService.create(payloadRequest)).isInstanceOf(RuntimeException.class);

        verify(noteRepository, times(1)).save(any(Note.class));
    }
}
