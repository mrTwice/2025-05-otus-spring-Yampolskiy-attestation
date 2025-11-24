package ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.exceptions;

public class TaskStatusAccessDeniedException extends TaskStatusException {
    public TaskStatusAccessDeniedException() {
        super("Access denied to task status resource.");
    }
}
