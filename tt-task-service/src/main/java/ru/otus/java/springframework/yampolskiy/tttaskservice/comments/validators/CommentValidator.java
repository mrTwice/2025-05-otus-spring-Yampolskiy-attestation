package ru.otus.java.springframework.yampolskiy.tttaskservice.comments.validators;


import ru.otus.java.springframework.yampolskiy.tttaskservice.comments.entities.Comment;
import ru.otus.java.springframework.yampolskiy.tttaskservice.comments.exceptions.InvalidCommentException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.common.DomainValidator;

public class CommentValidator implements DomainValidator<Comment> {

    @Override
    public void validateForCreate(Comment comment) {
        validateContent(comment);
    }

    @Override
    public void validateForUpdate(Comment existing, Comment updated) {
        validateContent(updated);
        if (!existing.getAuthorId().equals(updated.getAuthorId())) {
            throw new InvalidCommentException("Author of comment cannot be changed");
        }
        if (!existing.getTaskId().equals(updated.getTaskId())) {
            throw new InvalidCommentException("Task ID of comment cannot be changed");
        }
    }

    private void validateContent(Comment comment) {
        if (comment.getContent() == null || comment.getContent().isBlank()) {
            throw new InvalidCommentException("Comment content must not be blank");
        }
        if (comment.getContent().length() > 5000) {
            throw new InvalidCommentException("Comment is too long (max 5000 characters)");
        }
    }
}

