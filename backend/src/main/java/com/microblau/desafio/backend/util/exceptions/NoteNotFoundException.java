package com.microblau.desafio.backend.util.exceptions;

public class NoteNotFoundException extends RuntimeException {
    public NoteNotFoundException() {
        super("Nota não encontrada");
    };
    public NoteNotFoundException(String message) {
        super(message);
    }
}
