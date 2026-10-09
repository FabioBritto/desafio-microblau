package com.microblau.desafio.backend.service;

import com.microblau.desafio.backend.controller.note.dto.CreateNoteDTO;
import com.microblau.desafio.backend.controller.note.dto.NoteDTO;
import com.microblau.desafio.backend.controller.note.dto.UpdateNoteDTO;
import com.microblau.desafio.backend.model.note.Note;
import com.microblau.desafio.backend.repository.NoteRepository;
import com.microblau.desafio.backend.service.note.NoteServiceImpl;
import com.microblau.desafio.backend.util.exceptions.InvalidDateException;
import com.microblau.desafio.backend.util.exceptions.NoteNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Captor
    private ArgumentCaptor<Note> noteCaptor;

    @InjectMocks
    private NoteServiceImpl noteService;

    private CreateNoteDTO payloadRequest;

    private final Pageable pageable = PageRequest.of(0, 10);

    private static final Instant START = Instant.parse("2024-08-01T00:00:00Z");
    private static final Instant END = Instant.parse("2024-08-02T00:00:00Z");

    private Note buildNote(String id) {
        Note note = new Note();
        note.setId(id);
        note.setSite("https://site-original.com");
        note.setEquipment("Compressor 01");
        note.setVariable("temperatura");
        note.setAuthor("Maria Silva");
        note.setMessage("Mensagem original");
        note.setTimestamp(Timestamp.from(START));
        return note;
    }

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
    @DisplayName("Deveria lançar exceção por erro ao criar")
    void create_shouldThrowException() {
        when(noteRepository.save(any(Note.class)))
                .thenThrow(new RuntimeException("Não foi possível cadastrar uma Nota"));

        assertThatThrownBy(() -> noteService.create(payloadRequest)).isInstanceOf(RuntimeException.class);

        verify(noteRepository, times(1)).save(any(Note.class));
    }


    @Test
    @DisplayName("Deveria repassar os filtros ao repositório e mapear para DTO")
    void findAll_shouldDelegateToRepositoryAndMapToDto() {
        Note note = buildNote("abc");
        when(noteRepository.findAll(eq("SiteA"), eq("Equip01"), any(), any(), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(note), pageable, 1));

        Page<NoteDTO> result = noteService.findAll(pageable, "SiteA", "Equip01", null, null);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).hasSize(1);
        NoteDTO dto = result.getContent().getFirst();
        assertThat(dto.id()).isEqualTo("abc");
        assertThat(dto.site()).isEqualTo(note.getSite());
        assertThat(dto.equipment()).isEqualTo(note.getEquipment());
        assertThat(dto.variable()).isEqualTo(note.getVariable());
        assertThat(dto.author()).isEqualTo(note.getAuthor());
        assertThat(dto.message()).isEqualTo(note.getMessage());
    }

    @Test
    @DisplayName("Deveria converter site e equipment em branco para null")
    void findAll_shouldConvertBlankStringsToNull() {
        when(noteRepository.findAll(any(), any(), any(), any(), eq(pageable)))
                .thenReturn(Page.empty());

        noteService.findAll(pageable, "   ", "", null, null);

        verify(noteRepository).findAll(isNull(), isNull(), isNull(), isNull(), eq(pageable));
    }

    @Test
    @DisplayName("Deveria converter as datas no formato do CSV (ISO-8601) para Timestamp antes de consultar")
    void findAll_shouldConvertDatesToTimestamp() {
        when(noteRepository.findAll(any(), any(), any(), any(), eq(pageable)))
                .thenReturn(Page.empty());

        noteService.findAll(pageable, null, null, START, END);

        verify(noteRepository).findAll(
                isNull(),
                isNull(),
                eq(Timestamp.from(START)),
                eq(Timestamp.from(END)),
                eq(pageable));
    }

    @Test
    @DisplayName("Deveria aceitar apenas startDate")
    void findAll_shouldAcceptOnlyStartDate() {
        when(noteRepository.findAll(any(), any(), any(), any(), eq(pageable)))
                .thenReturn(Page.empty());

        noteService.findAll(pageable, null, null, START, null);

        verify(noteRepository).findAll(
                isNull(), isNull(), eq(Timestamp.from(START)), isNull(), eq(pageable));
    }

    @Test
    @DisplayName("Deveria aceitar startDate igual a endDate")
    void findAll_shouldAcceptEqualDates() {
        when(noteRepository.findAll(any(), any(), any(), any(), eq(pageable)))
                .thenReturn(Page.empty());

        noteService.findAll(pageable, null, null, START, START);

        verify(noteRepository).findAll(any(), any(), any(), any(), eq(pageable));
    }

    @Test
    @DisplayName("Não deveria consultar o repositório quando startDate > endDate")
    void findAll_shouldThrowWhenStartIsAfterEnd() {
        assertThatThrownBy(() -> noteService.findAll(pageable, null, null, END, START))
                .isInstanceOf(InvalidDateException.class);

        verifyNoInteractions(noteRepository);
    }

    @Test
    @DisplayName("Deveria atualizar os campos editáveis e retornar o DTO")
    void update_shouldUpdateFieldsAndReturnDto() {
        Note existing = buildNote("abc");
        UpdateNoteDTO dto = new UpdateNoteDTO(
                "https://site-novo.com",
                "Compressor 02",
                "pressao",
                "Mensagem nova"
        );
        when(noteRepository.findById("abc")).thenReturn(Optional.of(existing));
        when(noteRepository.save(any(Note.class))).thenAnswer(invocation -> invocation.getArgument(0));

        NoteDTO result = noteService.update("abc", dto);

        verify(noteRepository).save(noteCaptor.capture());
        Note saved = noteCaptor.getValue();

        assertThat(saved.getSite()).isEqualTo("https://site-novo.com");
        assertThat(saved.getEquipment()).isEqualTo("Compressor 02");
        assertThat(saved.getVariable()).isEqualTo("pressao");
        assertThat(saved.getMessage()).isEqualTo("Mensagem nova");

        assertThat(result.id()).isEqualTo("abc");
        assertThat(result.site()).isEqualTo("https://site-novo.com");
        assertThat(result.message()).isEqualTo("Mensagem nova");
    }

    @Test
    @DisplayName("Não deveria alterar id, autor e timestamp")
    void update_shouldNotChangeIdAuthorAndTimestamp() {
        Note existing = buildNote("abc");
        Timestamp originalTimestamp = existing.getTimestamp();
        UpdateNoteDTO dto = new UpdateNoteDTO("https://site-novo.com", "Compressor 02", "pressao", "Nova");
        when(noteRepository.findById("abc")).thenReturn(Optional.of(existing));
        when(noteRepository.save(any(Note.class))).thenAnswer(inv -> inv.getArgument(0));

        noteService.update("abc", dto);

        verify(noteRepository).save(noteCaptor.capture());
        Note saved = noteCaptor.getValue();
        assertThat(saved.getId()).isEqualTo("abc");
        assertThat(saved.getAuthor()).isEqualTo("Maria Silva");
        assertThat(saved.getTimestamp()).isEqualTo(originalTimestamp);
    }

    @Test
    @DisplayName("Não deveria salvar quando a note não existe")
    void update_shouldThrowWhenNoteNotFound() {
        UpdateNoteDTO dto = new UpdateNoteDTO("https://site-novo.com", "Compressor 02", "pressao", "Nova");
        when(noteRepository.findById("inexistente")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> noteService.update("inexistente", dto))
                .isInstanceOf(NoteNotFoundException.class);

        verify(noteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deveria remover a note existente")
    void delete_shouldRemoveExistingNote() {
        when(noteRepository.existsById("abc")).thenReturn(true);

        noteService.delete("abc");

        verify(noteRepository).deleteById("abc");
    }

    @Test
    @DisplayName("Não deveria remover quando a note não existe")
    void delete_shouldThrowWhenNoteNotFound() {
        when(noteRepository.existsById("abc")).thenReturn(false);

        assertThatThrownBy(() -> noteService.delete("abc"))
                .isInstanceOf(NoteNotFoundException.class);

        verify(noteRepository, never()).deleteById(any());
    }
}
