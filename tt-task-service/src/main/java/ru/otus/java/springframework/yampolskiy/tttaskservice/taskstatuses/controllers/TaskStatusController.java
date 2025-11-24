package ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.controllers;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.dtos.TaskStatusListResponse;
import ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.dtos.TaskStatusResponse;
import ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.dtos.TaskStatusShortResponse;
import ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.dtos.TaskStatusCreateRequest;
import ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.dtos.TaskStatusUpdateRequest;
import ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.entities.TaskStatus;
import ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.mappers.TaskStatusMapper;
import ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.services.TaskStatusService;

import java.util.List;
import java.util.UUID;

@Tag(name = "Task Statuses", description = "Операции со статусами задач")
@SecurityRequirement(name = "oidc")
@RestController
@RequestMapping("/api/v1/task-statuses")
@RequiredArgsConstructor
@Validated
public class TaskStatusController {

    private final TaskStatusService taskStatusService;

    private final TaskStatusMapper taskStatusMapper;

    @Operation(
            summary = "Получить все статусы задач",
            description = "Возвращает отсортированный список всех статусов задач")
    @ApiResponse(
            responseCode = "200",
            description = "Успешно",
            content = @Content(schema = @Schema(implementation = TaskStatusListResponse.class)))
    @GetMapping
    @PreAuthorize("@taskStatusAccessPolicy.canViewTaskStatus(authentication)")
    public TaskStatusListResponse getAll() {
        List<TaskStatus> statuses = taskStatusService.findAllSorted();
        List<TaskStatusResponse> responses = taskStatusMapper.toResponseList(statuses);
        return new TaskStatusListResponse(responses, responses.size());
    }

    @Operation(summary = "Получить статусы по умолчанию")
    @ApiResponse(
            responseCode = "200",
            description = "Успешно",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskStatusShortResponse.class))))
    @GetMapping("/default")
    @PreAuthorize("@taskStatusAccessPolicy.canViewTaskStatus(authentication)")
    public List<TaskStatusShortResponse> getDefaultStatuses() {
        return taskStatusMapper.toShortResponseList(taskStatusService.findDefaults());
    }

    @Operation(summary = "Получить финальные или не финальные статусы", description = "Поиск по флагу isFinal")
    @ApiResponse(
            responseCode = "200",
            description = "Успешно",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskStatusShortResponse.class))))
    @GetMapping("/final")
    @PreAuthorize("@taskStatusAccessPolicy.canViewTaskStatus(authentication)")
    public List<TaskStatusShortResponse> getFinalStatuses(
            @RequestParam(defaultValue = "true")
            @Parameter(description = "true — финальные, false — не финальные") boolean isFinal) {
        return taskStatusMapper.toShortResponseList(taskStatusService.findFinal(isFinal));
    }

    @Operation(summary = "Получить статус задачи по ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Найден",
                    content = @Content(schema = @Schema(implementation = TaskStatusResponse.class))),
            @ApiResponse(responseCode = "404", description = "Не найден")
    })
    @GetMapping("/{id}")
    @PreAuthorize("@taskStatusAccessPolicy.canViewTaskStatus(authentication)")
    public TaskStatusResponse getById(
            @Parameter(description = "ID статуса задачи") @PathVariable UUID id) {
        TaskStatus status = taskStatusService.findById(id);
        return taskStatusMapper.toResponse(status);
    }

    @Operation(summary = "Получить статус задачи по коду")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Найден",
                    content = @Content(schema = @Schema(implementation = TaskStatusResponse.class))),
            @ApiResponse(responseCode = "404", description = "Не найден")
    })
    @GetMapping("/by-code/{code}")
    @PreAuthorize("@taskStatusAccessPolicy.canViewTaskStatus(authentication)")
    public TaskStatusResponse getByCode(
            @Parameter(description = "Код статуса") @PathVariable String code) {
        TaskStatus status = taskStatusService.findByCode(code);
        return taskStatusMapper.toResponse(status);
    }

    @Operation(summary = "Поиск статусов задач", description = "Фильтрация по имени")
    @ApiResponse(
            responseCode = "200",
            description = "Успешно",
            content = @Content(schema = @Schema(implementation = TaskStatusResponse.class)))
    @GetMapping("/search")
    @PreAuthorize("@taskStatusAccessPolicy.canViewTaskStatus(authentication)")
    public Page<TaskStatusResponse> search(
            @RequestParam @Parameter(description = "Поисковая строка") String q,
            Pageable pageable) {
        return taskStatusService.search(q, pageable).map(taskStatusMapper::toResponse);
    }

    @Operation(summary = "Создать статус задачи")
    @ApiResponse(
            responseCode = "200",
            description = "Создан",
            content = @Content(schema = @Schema(implementation = TaskStatusResponse.class)))
    @PostMapping
    @PreAuthorize("@taskStatusAccessPolicy.canCreateTaskStatus(authentication)")
    public TaskStatusResponse create(
            @RequestBody @Valid @Parameter(description = "Данные для создания") TaskStatusCreateRequest request) {
        TaskStatus entity = taskStatusMapper.toEntity(request);
        TaskStatus created = taskStatusService.create(entity);
        return taskStatusMapper.toResponse(created);
    }

    @Operation(summary = "Обновить статус задачи")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Обновлён",
                    content = @Content(schema = @Schema(implementation = TaskStatusResponse.class))),
            @ApiResponse(responseCode = "404", description = "Не найден")
    })
    @PutMapping("/{id}")
    @PreAuthorize("@taskStatusAccessPolicy.canUpdateTaskStatus(authentication)")
    public TaskStatusResponse update(
            @PathVariable @Parameter(description = "ID статуса задачи") UUID id,
            @RequestBody @Valid @Parameter(description = "Обновлённые данные") TaskStatusUpdateRequest request) {
        TaskStatus existing = taskStatusService.findById(id);
        taskStatusMapper.updateEntity(existing, request);
        TaskStatus updated = taskStatusService.update(id, existing);
        return taskStatusMapper.toResponse(updated);
    }

    @Operation(summary = "Удалить статус задачи")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Удалён"),
            @ApiResponse(responseCode = "404", description = "Не найден")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("@taskStatusAccessPolicy.canDeleteTaskStatus(authentication)")
    public void delete(
            @PathVariable @Parameter(description = "ID статуса задачи") UUID id) {
        taskStatusService.delete(id);
    }
}
