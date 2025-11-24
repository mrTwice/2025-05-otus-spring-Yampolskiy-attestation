package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.exceptions.taskpriority;

import java.util.UUID;

public class TaskPriorityNotFoundException extends TaskPriorityException {

    public TaskPriorityNotFoundException(String code) {
        super("TaskPriority not found by code: " + code);
    }

    public TaskPriorityNotFoundException(UUID id) {
        super("TaskPriority not found by id: " + id);
    }
}

