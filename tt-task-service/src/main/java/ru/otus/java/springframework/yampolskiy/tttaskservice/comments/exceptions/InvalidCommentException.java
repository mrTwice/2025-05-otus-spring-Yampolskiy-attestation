package ru.otus.java.springframework.yampolskiy.tttaskservice.comments.exceptions;

public class InvalidCommentException extends CommentException {
    public InvalidCommentException(String message) {
        super(message);
    }
}
