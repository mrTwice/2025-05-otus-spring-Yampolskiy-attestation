package ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.exceptions;

public class InvalidTaskStatusException extends TaskStatusException {
    public InvalidTaskStatusException(String message) {
        super(message);
    }
}
