package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.exceptions.task;

import java.util.UUID;

public class TaskAlreadyDeletedException extends TaskException {
    public TaskAlreadyDeletedException(UUID id) {
        super("Task already deleted with id: " + id);
    }
}