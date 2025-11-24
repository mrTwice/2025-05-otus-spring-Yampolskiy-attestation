package ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.exceptions;

public class TaskStatusException extends RuntimeException {
    public TaskStatusException(String message) {
        super(message);
    }
}
