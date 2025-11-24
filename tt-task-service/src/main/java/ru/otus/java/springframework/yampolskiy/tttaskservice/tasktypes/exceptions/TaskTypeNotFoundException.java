package ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.exceptions;

import java.util.UUID;

public class TaskTypeNotFoundException extends TaskTypeException {
    public TaskTypeNotFoundException(UUID id) {
        super("TaskType not found by id: " + id);
    }

    public TaskTypeNotFoundException(String code) {
        super("TaskType not found by code: " + code);
    }
}

