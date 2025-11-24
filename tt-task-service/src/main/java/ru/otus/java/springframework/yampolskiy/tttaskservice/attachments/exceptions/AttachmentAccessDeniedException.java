package ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.exceptions;

public class AttachmentAccessDeniedException extends AttachmentException {
    public AttachmentAccessDeniedException() {
        super("Access denied to attachment resource.");
    }
}
