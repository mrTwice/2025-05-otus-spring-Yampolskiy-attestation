package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.exceptions.task;

public class InvalidTaskException extends TaskException {
    public InvalidTaskException(String message) {
        super(message);
    }
}
