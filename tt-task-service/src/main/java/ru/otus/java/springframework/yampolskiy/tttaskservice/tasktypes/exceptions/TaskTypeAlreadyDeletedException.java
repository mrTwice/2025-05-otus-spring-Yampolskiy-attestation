package ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.exceptions;

import java.util.UUID;

public class TaskTypeAlreadyDeletedException extends TaskTypeException {
    public TaskTypeAlreadyDeletedException(UUID id) {
        super("TaskType already deleted with id: " + id);
    }
}