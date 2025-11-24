package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.validators;

import ru.otus.java.springframework.yampolskiy.tttaskservice.common.DomainValidator;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.entities.Task;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.exceptions.task.InvalidTaskException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.exceptions.task.TaskCompletedUpdateException;

import java.time.Instant;

public class TaskValidator implements DomainValidator<Task> {

    @Override
    public void validateForCreate(Task task) {
        validateRequiredFields(task);
        validateDueDate(task.getDueDate(), Instant.now());
        validateSelfParent(task);
    }

    @Override
    public void validateForUpdate(Task existingTask, Task updatedTask) {
        if (existingTask.getCompletedAt() != null) {
            throw new TaskCompletedUpdateException(existingTask.getId());
        }

        validateRequiredFields(updatedTask);
        validateDueDate(updatedTask.getDueDate(), existingTask.getCreatedAt());
        validateSelfParent(updatedTask);
    }

    private void validateRequiredFields(Task task) {
        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new InvalidTaskException("Title is required");
        }
        if (task.getStatusCode() == null || task.getStatusCode().isBlank()) {
            throw new InvalidTaskException("Status code is required");
        }
        if (task.getStatusId() == null) {
            throw new InvalidTaskException("Status ID is required");
        }
        if (task.getTypeCode() == null || task.getTypeCode().isBlank()) {
            throw new InvalidTaskException("Type code is required");
        }
        if (task.getTypeId() == null) {
            throw new InvalidTaskException("Type ID is required");
        }
    }

    private void validateDueDate(Instant dueDate, Instant referenceDate) {
        if (dueDate != null && referenceDate != null && dueDate.isBefore(referenceDate)) {
            throw new InvalidTaskException("Due date cannot be before creation time");
        }
    }

    private void validateSelfParent(Task task) {
        if (task.getParent() != null && task.getId() != null &&
                task.getParent().getId().equals(task.getId())) {
            throw new InvalidTaskException("Task cannot be its own parent");
        }
    }
}



