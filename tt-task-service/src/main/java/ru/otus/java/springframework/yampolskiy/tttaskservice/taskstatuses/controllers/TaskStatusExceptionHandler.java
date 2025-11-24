package ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.java.springframework.yampolskiy.tttaskservice.common.dto.ErrorDTO;
import ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.exceptions.InvalidTaskStatusException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.exceptions.TaskStatusAccessDeniedException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.exceptions.TaskStatusNotFoundException;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TaskStatusExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskStatusExceptionHandler.class);

    @ExceptionHandler(TaskStatusNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFound(TaskStatusNotFoundException ex) {
        return new ResponseEntity<>(new ErrorDTO("TASK_STATUS_NOT_FOUND", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidTaskStatusException.class)
    public ResponseEntity<ErrorDTO> handleInvalid(InvalidTaskStatusException ex) {
        return new ResponseEntity<>(
                new ErrorDTO("TASK_STATUS_INVALID", ex.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TaskStatusAccessDeniedException.class)
    public ResponseEntity<ErrorDTO> handleCommentAccessDenied(TaskStatusAccessDeniedException e) {
        return new ResponseEntity<>(new ErrorDTO("TASK_STATUS_ACCESS_DENIED", e.getMessage()), HttpStatus.FORBIDDEN);
    }

}

