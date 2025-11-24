package ru.otus.java.springframework.yampolskiy.tttaskservice.comments.exceptions;

import java.util.UUID;

public class CommentNotFoundException extends CommentException {
    public CommentNotFoundException(UUID id) {
        super("Comment not found with id: " + id);
    }
}
