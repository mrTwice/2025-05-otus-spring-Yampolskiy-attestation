package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.exceptions.task;

public class TaskException extends RuntimeException {
    public TaskException(String message) {
        super(message);
    }
}
