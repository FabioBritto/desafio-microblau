package com.microblau.desafio.backend.config;

import com.microblau.desafio.backend.util.exceptions.seed.SeedException;
import jakarta.annotation.PostConstruct;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Seed {

    private static final String SEED_PATH = "db/seed/notes.csv";

    @PostConstruct
    public void seed() {

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(SEED_PATH);

        if(inputStream == null) throw new IllegalStateException("Seed não encontrada no endereço: " + SEED_PATH);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;

            while((line = br.readLine()) != null) {

            }
        } catch (IOException ex) {

        }

    }
}
