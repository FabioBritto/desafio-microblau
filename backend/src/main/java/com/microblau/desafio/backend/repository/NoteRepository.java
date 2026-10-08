package com.microblau.desafio.backend.repository;

import com.microblau.desafio.backend.model.note.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoteRepository extends JpaRepository<Note, String> {

    @Query("""
    SELECT n FROM Note n
    WHERE (:site IS NULL OR n.site = :site)
    AND (:equipment IS NULL OR n.site = :equipment)
    AND (:startDate IS NULL OR n.timestamp = :startDate)
    AND (:endDate IS NULL OR n.timestamp = :endDate)
    """)
    Page<Note> findAll(@Param("site") String site,
                       @Param("equipment") String equipment,
                       @Param("startDate") String startDate,
                       @Param("endDate") String endDate,
                       Pageable pageable);

}
