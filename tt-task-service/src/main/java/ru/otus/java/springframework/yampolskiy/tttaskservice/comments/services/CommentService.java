package ru.otus.java.springframework.yampolskiy.tttaskservice.comments.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.java.springframework.yampolskiy.tttaskservice.comments.entities.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentService {

    Comment create(Comment comment);

    Comment update(UUID commentId, Comment updatedComment);

    void delete(UUID commentId);

    Comment findById(UUID id);

    List<Comment> findByTaskId(UUID taskId);

    List<Comment> findByAuthorId(UUID authorId);

    Page<Comment> search(UUID taskId, UUID authorId, String text, Pageable pageable);

    long countByTaskId(UUID taskId);
}
