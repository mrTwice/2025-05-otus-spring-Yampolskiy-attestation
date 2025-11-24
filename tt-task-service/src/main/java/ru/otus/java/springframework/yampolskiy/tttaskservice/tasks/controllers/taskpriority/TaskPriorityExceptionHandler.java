package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.controllers.taskpriority;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.otus.java.springframework.yampolskiy.tttaskservice.common.dto.ErrorDTO;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.exceptions.taskpriority.InvalidTaskPriorityException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.exceptions.taskpriority.TaskPriorityAccessDeniedException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.exceptions.taskpriority.TaskPriorityAlreadyDeletedException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.exceptions.taskpriority.TaskPriorityNotFoundException;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TaskPriorityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskPriorityExceptionHandler.class);

    @ExceptionHandler(TaskPriorityNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleNotFound(TaskPriorityNotFoundException ex) {
        return new ResponseEntity<>(new ErrorDTO("TASK_PRIORITY_NOT_FOUND", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidTaskPriorityException.class)
    public ResponseEntity<ErrorDTO> handleInvalid(InvalidTaskPriorityException ex) {
        return new ResponseEntity<>(
                new ErrorDTO(
                        "TASK_PRIORITY_INVALID",
                        ex.getMessage()
                ),
                HttpStatus.UNPROCESSABLE_ENTITY
        );
    }

    @ExceptionHandler(TaskPriorityAlreadyDeletedException.class)
    public ResponseEntity<ErrorDTO> handleAlreadyDeleted(TaskPriorityAlreadyDeletedException ex) {
        return new ResponseEntity<>(new ErrorDTO("TASK_PRIORITY_ALREADY_DELETED", ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TaskPriorityAccessDeniedException.class)
    public ResponseEntity<ErrorDTO> handleCommentAccessDenied(TaskPriorityAccessDeniedException e) {
        return new ResponseEntity<>(
                new ErrorDTO(
                        "TASK_PRIORITY_ACCESS_DENIED",
                        e.getMessage()),
                HttpStatus.FORBIDDEN
        );
    }
}
