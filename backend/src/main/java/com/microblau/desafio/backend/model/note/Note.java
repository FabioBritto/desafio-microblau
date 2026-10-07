package com.microblau.desafio.backend.model.note;

import com.microblau.desafio.backend.model.note.enums.Equipment;
import com.microblau.desafio.backend.model.note.enums.Variable;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "notes")
public class Note {

    @Id
    @Column(length = 255, nullable = false)
    private String id;

    @Column(length = 255, nullable = false)
    private String site;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Equipment equipment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Variable variable;

    @CreationTimestamp
    private Timestamp timestamp;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, columnDefinition = "text")
    private String message;

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Variable getVariable() {
        return variable;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
