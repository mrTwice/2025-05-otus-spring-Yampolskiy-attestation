package ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.java.springframework.yampolskiy.tttaskservice.common.dto.ErrorDTO;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.exceptions.InvalidTaskTypeException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.exceptions.TaskTypeAccessDeniedException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.exceptions.TaskTypeAlreadyDeletedException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.exceptions.TaskTypeNotFoundException;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TaskTypeExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskTypeExceptionHandler.class);

    @ExceptionHandler(TaskTypeNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFound(TaskTypeNotFoundException ex) {
        return new ResponseEntity<>(new ErrorDTO("TASK_TYPE_NOT_FOUND", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidTaskTypeException.class)
    public ResponseEntity<ErrorDTO> handleInvalid(InvalidTaskTypeException ex) {
        return new ResponseEntity<>(new ErrorDTO("TASK_TYPE_INVALID", ex.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TaskTypeAlreadyDeletedException.class)
    public ResponseEntity<ErrorDTO> handleAlreadyDeleted(TaskTypeAlreadyDeletedException ex) {
        return new ResponseEntity<>(new ErrorDTO("TASK_TYPE_ALREADY_DELETED", ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TaskTypeAccessDeniedException.class)
    public ResponseEntity<ErrorDTO> handleCommentAccessDenied(TaskTypeAccessDeniedException e) {
        return new ResponseEntity<>(new ErrorDTO("TASK_TYPE_ACCESS_DENIED", e.getMessage()), HttpStatus.FORBIDDEN);
    }
}
