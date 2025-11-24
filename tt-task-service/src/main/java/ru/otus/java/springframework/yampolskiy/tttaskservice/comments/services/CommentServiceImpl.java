package ru.otus.java.springframework.yampolskiy.tttaskservice.comments.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.springframework.yampolskiy.tttaskservice.comments.entities.Comment;
import ru.otus.java.springframework.yampolskiy.tttaskservice.comments.exceptions.CommentNotFoundException;
import ru.otus.java.springframework.yampolskiy.tttaskservice.comments.repositories.CommentRepository;
import ru.otus.java.springframework.yampolskiy.tttaskservice.common.DomainValidator;

import java.util.List;
import java.util.UUID;

@Service("commentService")
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final DomainValidator<Comment> commentValidator;

    @Override
    public Comment create(Comment comment) {
        commentValidator.validateForCreate(comment);
        return commentRepository.save(comment);
    }

    @Override
    public Comment update(UUID commentId, Comment updatedComment) {
        Comment existing = getByIdOrThrow(commentId);
        commentValidator.validateForUpdate(existing, updatedComment);

        existing.setContent(updatedComment.getContent());
        return commentRepository.save(existing);
    }

    @Override
    public void delete(UUID commentId) {
        Comment comment = getByIdOrThrow(commentId);
        commentRepository.delete(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public Comment findById(UUID id) {
        return getByIdOrThrow(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findByTaskId(UUID taskId) {
        return commentRepository.findByTaskIdOrderByCreatedAtAsc(taskId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findByAuthorId(UUID authorId) {
        return commentRepository.findByAuthorId(authorId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Comment> search(UUID taskId, UUID authorId, String text, Pageable pageable) {
        return commentRepository.searchComments(taskId, authorId, text, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByTaskId(UUID taskId) {
        return commentRepository.countByTaskId(taskId);
    }

    private Comment getByIdOrThrow(UUID id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));
    }
}

