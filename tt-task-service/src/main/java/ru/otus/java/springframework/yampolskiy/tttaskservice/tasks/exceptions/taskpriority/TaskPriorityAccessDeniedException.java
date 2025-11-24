package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.exceptions.taskpriority;

public class TaskPriorityAccessDeniedException extends TaskPriorityException {
    public TaskPriorityAccessDeniedException() {
        super("Access denied to task priority resource.");
    }
}
