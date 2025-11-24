package ru.otus.java.springframework.yampolskiy.tttaskservice.comments.exceptions;

public class CommentException extends RuntimeException {
    public CommentException(String message) {
        super(message);
    }
}
