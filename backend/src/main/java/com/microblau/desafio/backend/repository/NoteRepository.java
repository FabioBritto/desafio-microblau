package com.microblau.desafio.backend.repository;

import com.microblau.desafio.backend.model.note.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, String> {


    //Page<Note> findAllPageable(Pageable pageable);
}
