package ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.exceptions;

public class InvalidTaskTypeException extends TaskTypeException {
    public InvalidTaskTypeException(String message) {
        super(message);
    }
}
