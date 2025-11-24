package ru.otus.java.springframework.yampolskiy.ttuserservice.controllers;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.error.ErrorDTO;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.error.ValidationEmailErrorDTO;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.error.ValidationEmailPartErrorDTO;
import ru.otus.java.springframework.yampolskiy.ttuserservice.exceptions.DuplicateResourceException;
import ru.otus.java.springframework.yampolskiy.ttuserservice.exceptions.ResourceNotFoundException;
import ru.otus.java.springframework.yampolskiy.ttuserservice.exceptions.ValidationEmailException;
import ru.otus.java.springframework.yampolskiy.ttuserservice.exceptions.ValidationException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleResourceNotFoundException(ResourceNotFoundException e) {
        return new ResponseEntity<>(new ErrorDTO("RESOURCE_NOT_FOUND", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorDTO> handleDuplicateResourceException(DuplicateResourceException e) {
        return new ResponseEntity<>(new ErrorDTO("CONFLICT", e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ValidationEmailException.class)
    public ResponseEntity<ValidationEmailErrorDTO> handleValidationEmailException(ValidationEmailException e) {
        return new ResponseEntity<>(
                new ValidationEmailErrorDTO(
                        e.getCode(),
                        e.getMessage(),
                        e.getErrors().stream()
                                .map(ve -> new ValidationEmailPartErrorDTO(ve.getField(), ve.getMessage()))
                                .toList()
                ),
                HttpStatus.UNPROCESSABLE_ENTITY
        );
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorDTO> handleValidationException(ValidationException e) {
        return new ResponseEntity<>(
                new ErrorDTO("UNPROCESSABLE_ENTITY", e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationEmailErrorDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        List<ValidationEmailPartErrorDTO> errors = e.getBindingResult().getFieldErrors().stream()
                .map(err -> new ValidationEmailPartErrorDTO(err.getField(), err.getDefaultMessage()))
                .toList();
        return new ResponseEntity<>(
                new ValidationEmailErrorDTO("EMAIL_VALIDATION_FAILED", "Ошибка валидации полей", errors),
                HttpStatus.UNPROCESSABLE_ENTITY
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationEmailErrorDTO> handleConstraintViolation(ConstraintViolationException e) {
        List<ValidationEmailPartErrorDTO> errors = e.getConstraintViolations().stream()
                .map(violation -> new ValidationEmailPartErrorDTO(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()))
                .toList();
        return new ResponseEntity<>(
                new ValidationEmailErrorDTO("VALIDATION_FAILED", "Ошибка валидации запроса", errors),
                HttpStatus.UNPROCESSABLE_ENTITY
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGenericException(Exception e) {
        LOGGER.error("Произошла непредвиденная ошибка", e);
        return new ResponseEntity<>(
                new ErrorDTO("INTERNAL_SERVER_ERROR", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

