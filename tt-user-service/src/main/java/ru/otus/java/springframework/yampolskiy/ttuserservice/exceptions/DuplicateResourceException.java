package ru.otus.java.springframework.yampolskiy.ttuserservice.exceptions;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
