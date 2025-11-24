package ru.otus.java.springframework.yampolskiy.tttaskservice.storage.exception;

import ru.otus.java.springframework.yampolskiy.tttaskservice.comments.exceptions.CommentException;

public class StorageAccessDeniedException extends CommentException {
    public StorageAccessDeniedException() {
        super("Access to the storage is denied.");
    }
}
