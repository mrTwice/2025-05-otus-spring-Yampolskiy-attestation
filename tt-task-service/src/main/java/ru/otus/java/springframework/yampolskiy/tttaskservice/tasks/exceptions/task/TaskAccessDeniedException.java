package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.exceptions.task;

public class TaskAccessDeniedException extends TaskException {
    public TaskAccessDeniedException() {
        super("Access denied to tasks resource.");
    }
}
