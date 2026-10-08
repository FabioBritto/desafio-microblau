package com.microblau.desafio.backend.controller;

import com.microblau.desafio.backend.controller.note.NoteController;
import com.microblau.desafio.backend.service.note.INoteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.verifyNoInteractions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoteController.class)
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private INoteService noteService;

    @Test
    @DisplayName("Deveria retornar HttStatus = 400 quando a data é inválida")
    void findAll_shouldReturn400WhenDataIsInvalid() throws Exception {
        mockMvc.perform(get("/api/v1/notes").param("startDate", "01/10/2026")).andExpect(status().isBadRequest());

        verifyNoInteractions(noteService);
    }
}
