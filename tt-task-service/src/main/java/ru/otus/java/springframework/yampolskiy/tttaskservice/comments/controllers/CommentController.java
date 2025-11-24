package ru.otus.java.springframework.yampolskiy.tttaskservice.comments.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.java.springframework.yampolskiy.tttaskservice.comments.dtos.CommentCreateRequest;
import ru.otus.java.springframework.yampolskiy.tttaskservice.comments.dtos.CommentListResponse;
import ru.otus.java.springframework.yampolskiy.tttaskservice.comments.dtos.CommentResponse;
import ru.otus.java.springframework.yampolskiy.tttaskservice.comments.dtos.CommentUpdateRequest;
import ru.otus.java.springframework.yampolskiy.tttaskservice.comments.entities.Comment;
import ru.otus.java.springframework.yampolskiy.tttaskservice.comments.mappers.CommentMapper;
import ru.otus.java.springframework.yampolskiy.tttaskservice.comments.services.CommentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Validated
@Tag(name = "Comments", description = "Управление комментариями к задачам")
@SecurityRequirement(name = "oidc")
public class CommentController {

    private final CommentService commentService;

    private final CommentMapper commentMapper;

    @Operation(summary = "Создать комментарий", description = "Создание нового комментария к задаче")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно создан",
                    content = @Content(schema = @Schema(implementation = CommentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка запроса"),
            @ApiResponse(responseCode = "403", description = "Нет прав на создание")
    })
    @PostMapping
    @PreAuthorize("@commentAccessPolicy.canCreateComment(authentication)")
    public CommentResponse create(
            @RequestBody @Valid CommentCreateRequest request,
            Authentication authentication
    ) {
        Comment comment = commentMapper.toEntity(request);
        comment.setAuthorId(getUserIdFromAuth(authentication));
        Comment created = commentService.create(comment);
        return commentMapper.toResponse(created);
    }

    @Operation(summary = "Обновить комментарий", description = "Обновляет содержимое комментария")
    @PutMapping("/{id}")
    @PreAuthorize("@commentAccessPolicy.canEditOwnCommentOnly(authentication, @commentService.findById(#id).authorId)")
    public CommentResponse update(
            @Parameter(description = "ID комментария")@PathVariable UUID id,
            @RequestBody @Valid CommentUpdateRequest request
    ) {
        Comment existing = commentService.findById(id);
        commentMapper.updateEntity(existing, request);
        Comment updated = commentService.update(id, existing);
        return commentMapper.toResponse(updated);
    }

    @Operation(summary = "Удалить комментарий", description = "Удаление комментария по ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("@commentAccessPolicy.canManageCommentOfUser(authentication, @commentService.findById(#id).authorId)")
    public void delete(
            @Parameter(description = "ID комментария") @PathVariable UUID id
    ) {
        commentService.delete(id);
    }

    @Operation(summary = "Получить комментарий", description = "Получить комментарий по ID")
    @GetMapping("/{id}")
    @PreAuthorize("@commentAccessPolicy.canViewComment(authentication)")
    public CommentResponse getById(
            @Parameter(description = "ID комментария") @PathVariable UUID id
    ) {
        return commentMapper.toResponse(commentService.findById(id));
    }

    @Operation(summary = "Комментарии по задаче", description = "Получить список комментариев по ID задачи")
    @GetMapping("/by-task/{taskId}")
    @PreAuthorize("@commentAccessPolicy.canViewComment(authentication)")
    public CommentListResponse getByTask(
            @Parameter(description = "ID задачи") @PathVariable UUID taskId
    ) {
        List<Comment> comments = commentService.findByTaskId(taskId);
        List<CommentResponse> responseList = commentMapper.toResponseList(comments);
        return new CommentListResponse(taskId, responseList);
    }

    @Operation(summary = "Поиск комментариев", description = "Поиск по фильтрам: задача, автор, текст")
    @GetMapping("/search")
    @PreAuthorize("@commentAccessPolicy.canViewComment(authentication)")
    public Page<CommentResponse> search(
            @Parameter(description = "ID задачи") @RequestParam(required = false) UUID taskId,
            @Parameter(description = "ID автора") @RequestParam(required = false) UUID authorId,
            @Parameter(description = "Текст комментария") @RequestParam(required = false) String text,
            Pageable pageable
    ) {
        return commentService.search(taskId, authorId, text, pageable)
                .map(commentMapper::toResponse);
    }

    @Operation(
            summary = "Количество комментариев по задаче",
            description = "Вернёт количество комментариев по ID задачи")
    @GetMapping("/count/by-task/{taskId}")
    @PreAuthorize("@commentAccessPolicy.canViewComment(authentication)")
    public long countByTask(
            @Parameter(description = "ID задачи") @PathVariable UUID taskId
    ) {
        return commentService.countByTaskId(taskId);
    }

    private UUID getUserIdFromAuth(Authentication authentication) {
        Jwt jwt = (authentication instanceof JwtAuthenticationToken jwtToken)
                ? jwtToken.getToken()
                : (authentication.getPrincipal() instanceof Jwt j ? j : null);

        if (jwt == null) {
            throw new AccessDeniedException("No valid JWT found");
        }
        return UUID.fromString(jwt.getSubject());
    }
}