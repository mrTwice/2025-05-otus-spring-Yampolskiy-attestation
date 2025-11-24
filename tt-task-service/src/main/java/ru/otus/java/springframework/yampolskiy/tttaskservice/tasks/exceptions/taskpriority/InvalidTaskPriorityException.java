package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.exceptions.taskpriority;

public class InvalidTaskPriorityException extends TaskPriorityException {
    public InvalidTaskPriorityException(String message) {
        super(message);
    }
}
