package ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.exceptions;

public class TaskTypeAccessDeniedException extends TaskTypeException {
    public TaskTypeAccessDeniedException() {
        super("Access denied to task type resource.");
    }
}
