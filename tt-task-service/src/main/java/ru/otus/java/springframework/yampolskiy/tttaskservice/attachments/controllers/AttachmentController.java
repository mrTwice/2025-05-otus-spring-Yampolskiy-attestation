package ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.controllers;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.dtos.AttachmentDeleteRequest;
import ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.dtos.AttachmentResponse;
import ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.dtos.AttachmentUpdateRequest;
import ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.dtos.AttachmentUploadRequest;
import ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.entities.Attachment;
import ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.mappers.AttachmentMapper;
import ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.services.AttachmentService;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/attachments")
@RequiredArgsConstructor
@Validated
@Tag(name = "Attachments", description = "Управление вложениями к задачам")
@SecurityRequirement(name = "oidc")
public class AttachmentController {

    private final AttachmentService attachmentService;

    private final AttachmentMapper attachmentMapper;

    @Operation(summary = "Загрузить метаинформацию файла",
            description = "Загружает метаинформацию уже загруженного файла (без содержимого).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Метаинформация успешно сохранена",
                    content = @Content(schema = @Schema(implementation = AttachmentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Ошибка запроса"),
            @ApiResponse(responseCode = "403", description = "Нет прав на загрузку")
    })
    @PostMapping
    @PreAuthorize("@attachmentAccessPolicy.canCreateAttachment(authentication)")
    public AttachmentResponse upload(
            @RequestBody @Valid AttachmentUploadRequest request,
            Authentication authentication
    ) {
        Attachment attachment = attachmentMapper.toEntity(request);
        attachment.setUploadedBy(getUserIdFromAuth(authentication));
        Attachment saved = attachmentService.upload(attachment);
        return attachmentMapper.toResponse(saved);
    }

    @Operation(summary = "Обновить метаинформацию вложения")
    @PutMapping("/{id}")
    @PreAuthorize("@attachmentAccessPolicy.canUpdateAttachment(authentication)")
    public AttachmentResponse update(
            @Parameter(description = "ID вложения") @PathVariable UUID id,
            @RequestBody @Valid AttachmentUpdateRequest request
    ) {
        Attachment existing = attachmentService.findById(id);
        attachmentMapper.updateEntity(existing, request);
        Attachment updated = attachmentService.update(id, existing);
        return attachmentMapper.toResponse(updated);
    }

    @Operation(summary = "Удалить вложение по ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("@attachmentAccessPolicy.canDeleteAttachment(authentication)")
    public void delete(
            @Parameter(description = "ID вложения") @PathVariable UUID id
    ) {
        attachmentService.delete(id);
    }

    @Operation(summary = "Удалить несколько вложений")
    @PostMapping("/delete")
    @PreAuthorize("@attachmentAccessPolicy.canDeleteAttachment(authentication)")
    public void deleteMany(
            @RequestBody @Valid AttachmentDeleteRequest request
    ) {
        attachmentService.deleteMany(request.getAttachmentIds());
    }

    @Operation(summary = "Получить вложение по ID")
    @GetMapping("/{id}")
    @PreAuthorize("@attachmentAccessPolicy.canViewAttachment(authentication)")
    public AttachmentResponse getById(
            @Parameter(description = "ID вложения") @PathVariable UUID id
    ) {
        return attachmentMapper.toResponse(attachmentService.findById(id));
    }

    @Operation(summary = "Получить все вложения по задаче")
    @GetMapping("/by-task/{taskId}")
    @PreAuthorize("@attachmentAccessPolicy.canViewAttachment(authentication)")
    public List<AttachmentResponse> getByTask(
            @Parameter(description = "ID задачи") @PathVariable UUID taskId
    ) {
        return attachmentMapper.toResponseList(attachmentService.findByTaskId(taskId));
    }

    @Operation(summary = "Получить вложения пользователя")
    @GetMapping("/by-uploader/{userId}")
    @PreAuthorize("@attachmentAccessPolicy.canViewAttachmentsOfUser(authentication, #userId)")
    public List<AttachmentResponse> getByUploader(
            @Parameter(description = "ID пользователя") @PathVariable UUID userId
    ) {
        return attachmentMapper.toResponseList(attachmentService.findByUploader(userId));
    }

    @Operation(summary = "Посчитать вложения по задаче")
    @GetMapping("/count/by-task/{taskId}")
    @PreAuthorize("@attachmentAccessPolicy.canViewAttachment(authentication)")
    public long countByTask(
            @Parameter(description = "ID задачи") @PathVariable UUID taskId
    ) {
        return attachmentService.countByTaskId(taskId);
    }

    @Operation(summary = "Получить суммарный размер вложений по задаче")
    @GetMapping("/size/by-task/{taskId}")
    @PreAuthorize("@attachmentAccessPolicy.canViewAttachment(authentication)")
    public Long getTotalSize(
            @Parameter(description = "ID задачи") @PathVariable UUID taskId
    ) {
        return attachmentService.getTotalSizeByTask(taskId);
    }

    @Operation(summary = "Поиск вложений по фильтрам")
    @GetMapping("/search")
    @PreAuthorize("@attachmentAccessPolicy.canViewAttachment(authentication)")
    public Page<AttachmentResponse> search(
            @Parameter(description = "ID задачи") @RequestParam(required = false) UUID taskId,
            @Parameter(description = "ID загрузившего пользователя") @RequestParam(required = false) UUID uploadedBy,
            @Parameter(description = "Название файла") @RequestParam(required = false) String fileName,
            Pageable pageable
    ) {
        return attachmentService.search(taskId, uploadedBy, fileName, pageable)
                .map(attachmentMapper::toResponse);
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
