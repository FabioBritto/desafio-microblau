package com.microblau.desafio.backend.config;

import com.microblau.desafio.backend.model.note.Note;
import com.microblau.desafio.backend.model.note.enums.Equipment;
import com.microblau.desafio.backend.repository.NoteRepository;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class Seed implements ApplicationRunner {

    private static final String SEED_PATH = "db/seed/notes.csv";

    private final NoteRepository noteRepository;

    public Seed(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if(noteRepository.count() > 0) {
            System.out.println("JÁ TEM DADOS");
            return;
        }

        List<Note> notes = new ArrayList<>();

        System.out.println("CHEGUEI AQUI");

        try (Reader reader = new InputStreamReader(
                new ClassPathResource(SEED_PATH).getInputStream(), StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                notes.add(fromCsvToEntity(line));
            }
        } catch (IOException | CsvValidationException ex) {
            //logar
            ex.printStackTrace();
        }

        noteRepository.saveAll(notes);

    }

    private Note fromCsvToEntity(String[] line) {
        Note note = new Note();
        note.setId(line[0]);
        note.setSite(line[1]);
        note.setEquipment(line[2]);
        note.setVariable(line[3]);
        note.setTimestamp(Timestamp.from(Instant.parse(line[4])));
        note.setAuthor(line[5]);
        note.setMessage(line[6]);
        return note;
    }
}
