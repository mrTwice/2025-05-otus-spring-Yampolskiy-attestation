package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.controllers.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.java.springframework.yampolskiy.tttaskservice.common.dto.ErrorDTO;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.exceptions.task.TaskNotFoundException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.exceptions.task.InvalidTaskException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.exceptions.task.TaskAlreadyDeletedException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.exceptions.task.TaskCompletedUpdateException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.exceptions.task.TaskAccessDeniedException;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TaskExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskExceptionHandler.class);

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFound(TaskNotFoundException ex) {
        return new ResponseEntity<>(new ErrorDTO("TASK_NOT_FOUND", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidTaskException.class)
    public ResponseEntity<ErrorDTO> handleInvalid(InvalidTaskException ex) {
        return new ResponseEntity<>(new ErrorDTO("TASK_INVALID", ex.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TaskAlreadyDeletedException.class)
    public ResponseEntity<ErrorDTO> handleAlreadyDeleted(TaskAlreadyDeletedException ex) {
        return new ResponseEntity<>(new ErrorDTO("TASK_ALREADY_DELETED", ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TaskCompletedUpdateException.class)
    public ResponseEntity<ErrorDTO> handleCompleted(TaskCompletedUpdateException ex) {
        return new ResponseEntity<>(new ErrorDTO("TASK_COMPLETED", ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TaskAccessDeniedException.class)
    public ResponseEntity<ErrorDTO> handleCommentAccessDenied(TaskAccessDeniedException e) {
        return new ResponseEntity<>(new ErrorDTO("TASK_ACCESS_DENIED", e.getMessage()), HttpStatus.FORBIDDEN);
    }

}
