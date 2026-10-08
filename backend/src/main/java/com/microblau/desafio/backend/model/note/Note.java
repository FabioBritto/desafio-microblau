package com.microblau.desafio.backend.model.note;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "notes")
public class Note {

    @Id
    @Column(length = 255, nullable = false)
    private String id;

    @Column(length = 255, nullable = false)
    private String site;

    @Column(length =255, nullable = false)
    private String equipment;

    @Column(length =255, nullable = false)
    private String variable;

    @CreationTimestamp
    private Timestamp timestamp;

    @Column(length =255, nullable = false)
    private String author;

    @Column(nullable = false, columnDefinition = "text")
    private String message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return Objects.equals(id, note.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Note{" +
                "id='" + id + '\'' +
                ", site='" + site + '\'' +
                ", equipment='" + equipment + '\'' +
                ", variable='" + variable + '\'' +
                ", timestamp=" + timestamp +
                ", author='" + author + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
