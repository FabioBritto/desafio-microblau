package com.microblau.desafio.backend.util.exceptions.dto;

import java.util.Map;

public record ValidationErrors(
        Map<String, String> errors
) {
}
