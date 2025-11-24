package ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.exceptions;

public class TaskTypeException extends RuntimeException {
    public TaskTypeException(String message) {
        super(message);
    }
}
