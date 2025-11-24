package ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.validators;

import ru.otus.java.springframework.yampolskiy.tttaskservice.common.DomainValidator;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.entities.TaskType;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.exceptions.InvalidTaskTypeException;

public class TaskTypeValidator implements DomainValidator<TaskType> {

    @Override
    public void validateForCreate(TaskType type) {
        validateCommon(type);
    }

    @Override
    public void validateForUpdate(TaskType existing, TaskType updated) {
        validateCommon(updated);
        if (!existing.getCode().equals(updated.getCode())) {
            throw new InvalidTaskTypeException("TaskType code cannot be changed");
        }
    }

    private void validateCommon(TaskType type) {
        if (type.getCode() == null || type.getCode().isBlank()) {
            throw new InvalidTaskTypeException("Code is required");
        }
        if (type.getName() == null || type.getName().isBlank()) {
            throw new InvalidTaskTypeException("Name is required");
        }
        if (type.getIcon() != null && type.getIcon().length() > 100) {
            throw new InvalidTaskTypeException("Icon must be 100 characters max");
        }
    }
}

