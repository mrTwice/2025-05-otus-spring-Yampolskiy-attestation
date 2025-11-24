package ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.exceptions.AttachmentAccessDeniedException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.exceptions.AttachmentNotFoundException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.exceptions.InvalidAttachmentException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.common.dto.ErrorDTO;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AttachmentExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttachmentExceptionHandler.class);

    @ExceptionHandler(AttachmentNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleAttachmentNotFoundException(AttachmentNotFoundException e) {
        return new ResponseEntity<>(new ErrorDTO("ATTACHMENT_NOT_FOUND", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidAttachmentException.class)
    public ResponseEntity<ErrorDTO> handleInvalidAttachmentException(InvalidAttachmentException e) {
        return new ResponseEntity<>(new ErrorDTO("ATTACHMENT_INVALID", e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(AttachmentAccessDeniedException.class)
    public ResponseEntity<ErrorDTO> handleAttachmentAccessDeniedException(AttachmentAccessDeniedException e) {
        return new ResponseEntity<>(new ErrorDTO("ATTACHMENT_ACCESS_DENIED", e.getMessage()), HttpStatus.FORBIDDEN);
    }
}

