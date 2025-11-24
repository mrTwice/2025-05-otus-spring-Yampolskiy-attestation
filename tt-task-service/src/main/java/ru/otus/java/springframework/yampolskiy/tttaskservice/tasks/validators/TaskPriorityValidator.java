package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.validators;


import ru.otus.java.springframework.yampolskiy.tttaskservice.common.DomainValidator;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.entities.TaskPriority;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.exceptions.taskpriority.InvalidTaskPriorityException;

import java.util.regex.Pattern;

public class TaskPriorityValidator implements DomainValidator<TaskPriority> {

    private static final Pattern HEX_COLOR = Pattern.compile("^#[0-9A-Fa-f]{6}$");

    @Override
    public void validateForCreate(TaskPriority priority) {
        validateRequired(priority);
        validateColor(priority.getColor());
        validateSortOrder(priority.getSortOrder());
    }

    @Override
    public void validateForUpdate(TaskPriority existing, TaskPriority updated) {
        if (!existing.getCode().equals(updated.getCode())) {
            throw new InvalidTaskPriorityException("TaskPriority code cannot be changed");
        }
        validateRequired(updated);
        validateColor(updated.getColor());
        validateSortOrder(updated.getSortOrder());
    }

    private void validateRequired(TaskPriority p) {
        if (p.getCode() == null || p.getCode().isBlank()) {
            throw new InvalidTaskPriorityException("Code is required");
        }
        if (p.getName() == null || p.getName().isBlank()) {
            throw new InvalidTaskPriorityException("Name is required");
        }
    }

    private void validateColor(String color) {
        if (color != null && !HEX_COLOR.matcher(color).matches()) {
            throw new InvalidTaskPriorityException("Color must be in format #RRGGBB");
        }
    }

    private void validateSortOrder(Integer sortOrder) {
        if (sortOrder != null && sortOrder < 0) {
            throw new InvalidTaskPriorityException("Sort order must be >= 0");
        }
    }
}

