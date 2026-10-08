package com.microblau.desafio.backend.util.exceptions;

import com.microblau.desafio.backend.util.exceptions.dto.ExceptionDTO;
import com.microblau.desafio.backend.util.exceptions.dto.ValidationErrors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> validationErrors = new HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(fieldError ->
                validationErrors.put(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()
                )
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ValidationErrors(validationErrors));

    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<ExceptionDTO> invalidDateHandler(InvalidDateException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDTO(exception.getMessage()));
    }

    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity<ExceptionDTO> noteNotFoundHandler(NoteNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDTO(exception.getMessage()));
    }

}
