package com.microblau.desafio.backend.repository;

import com.microblau.desafio.backend.model.note.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;

public interface NoteRepository extends JpaRepository<Note, String> {

    @Query("""
    SELECT n FROM Note n
    WHERE (:site IS NULL OR LOWER(n.site) LIKE LOWER(CONCAT('%', :site, '%')))
    AND (:equipment IS NULL OR LOWER(n.equipment) LIKE LOWER(CONCAT('%', :equipment, '%')))
    AND (:startDate IS NULL OR n.timestamp >= :startDate)
    AND (:endDate IS NULL OR n.timestamp <= :endDate)
    """)
    Page<Note> findAll(@Param("site") String site,
                       @Param("equipment") String equipment,
                       @Param("startDate") Timestamp startDate,
                       @Param("endDate") Timestamp endDate,
                       Pageable pageable);

}
