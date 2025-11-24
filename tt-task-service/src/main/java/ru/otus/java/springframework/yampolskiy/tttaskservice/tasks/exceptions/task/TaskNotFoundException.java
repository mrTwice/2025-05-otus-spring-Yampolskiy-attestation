package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.exceptions.task;

import java.util.UUID;

public class TaskNotFoundException extends TaskException {
  public TaskNotFoundException(UUID id) {
    super("Task not found with id: " + id);
  }
}
