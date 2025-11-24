package ru.otus.java.springframework.yampolskiy.ttuserservice.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
