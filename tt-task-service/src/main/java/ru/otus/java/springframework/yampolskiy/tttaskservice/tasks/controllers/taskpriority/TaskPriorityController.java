package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.controllers.taskpriority;

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
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.taskpriority.TaskPriorityCreateRequest;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.taskpriority.TaskPriorityResponse;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.taskpriority.TaskPriorityShortResponse;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.taskpriority.TaskPriorityUpdateRequest;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.entities.TaskPriority;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.mappers.TaskPriorityMapper;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.services.taskpriority.TaskPriorityService;


import java.util.List;
import java.util.UUID;

@Tag(name = "Task Priorities", description = "Операции с приоритетами задач")
@SecurityRequirement(name = "oidc")
@RestController
@RequestMapping("/api/v1/task-priorities")
@RequiredArgsConstructor
@Validated
public class TaskPriorityController {

    private final TaskPriorityService taskPriorityService;

    private final TaskPriorityMapper taskPriorityMapper;

    @Operation(
            summary = "Получить все приоритеты задач",
            description = "Возвращает отсортированный список всех приоритетов")
    @ApiResponse(
            responseCode = "200",
            description = "Успешно",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskPriorityResponse.class))))
    @GetMapping
    @PreAuthorize("@taskPriorityAccessPolicy.canViewTaskPriority(authentication)")
    public List<TaskPriorityResponse> getAll() {
        List<TaskPriority> priorities = taskPriorityService.findAllSorted();
        return taskPriorityMapper.toResponseList(priorities);
    }

    @Operation(summary = "Получить приоритеты задач по умолчанию")
    @ApiResponse(
            responseCode = "200",
            description = "Успешно",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskPriorityShortResponse.class))))
    @GetMapping("/default")
    @PreAuthorize("@taskPriorityAccessPolicy.canViewTaskPriority(authentication)")
    public List<TaskPriorityShortResponse> getDefaults() {
        return taskPriorityMapper.toShortResponseList(taskPriorityService.findDefaults());
    }

    @Operation(summary = "Получить приоритет по ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Найден",
                    content = @Content(schema = @Schema(implementation = TaskPriorityResponse.class))),
            @ApiResponse(responseCode = "404", description = "Не найден")
    })
    @GetMapping("/{id}")
    @PreAuthorize("@taskPriorityAccessPolicy.canViewTaskPriority(authentication)")
    public TaskPriorityResponse getById(
            @Parameter(description = "ID приоритета задачи") @PathVariable UUID id) {
        TaskPriority priority = taskPriorityService.findById(id);
        return taskPriorityMapper.toResponse(priority);
    }

    @Operation(summary = "Получить приоритет по коду")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Найден",
                    content = @Content(schema = @Schema(implementation = TaskPriorityResponse.class))),
            @ApiResponse(responseCode = "404", description = "Не найден")
    })
    @GetMapping("/by-code/{code}")
    @PreAuthorize("@taskPriorityAccessPolicy.canViewTaskPriority(authentication)")
    public TaskPriorityResponse getByCode(
            @Parameter(description = "Код приоритета задачи") @PathVariable String code) {
        TaskPriority priority = taskPriorityService.findByCode(code);
        return taskPriorityMapper.toResponse(priority);
    }

    @Operation(summary = "Создать новый приоритет задачи")
    @ApiResponse(
            responseCode = "200",
            description = "Создан",
            content = @Content(schema = @Schema(implementation = TaskPriorityResponse.class)))
    @PostMapping
    @PreAuthorize("@taskPriorityAccessPolicy.canCreateTaskPriority(authentication)")
    public TaskPriorityResponse create(
            @RequestBody
            @Valid
            @Parameter(description = "Данные для создания приоритета")
            TaskPriorityCreateRequest request
    ) {
        TaskPriority entity = taskPriorityMapper.toEntity(request);
        TaskPriority created = taskPriorityService.create(entity);
        return taskPriorityMapper.toResponse(created);
    }

    @Operation(summary = "Обновить приоритет задачи")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Обновлён",
                    content = @Content(schema = @Schema(implementation = TaskPriorityResponse.class))),
            @ApiResponse(responseCode = "404", description = "Не найден")
    })
    @PutMapping("/{id}")
    @PreAuthorize("@taskPriorityAccessPolicy.canUpdateTaskPriority(authentication)")
    public TaskPriorityResponse update(
            @PathVariable @Parameter(description = "ID приоритета") UUID id,
            @RequestBody @Valid @Parameter(description = "Новые данные приоритета") TaskPriorityUpdateRequest request) {
        TaskPriority existing = taskPriorityService.findById(id);
        taskPriorityMapper.updateEntity(existing, request);
        TaskPriority updated = taskPriorityService.update(id, existing);
        return taskPriorityMapper.toResponse(updated);
    }

    @Operation(summary = "Удалить приоритет задачи (мягкое удаление)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Удалено"),
            @ApiResponse(responseCode = "404", description = "Не найден"),
            @ApiResponse(responseCode = "409", description = "Уже удалён")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("@taskPriorityAccessPolicy.canDeleteTaskPriority(authentication)")
    public void delete(
            @PathVariable @Parameter(description = "ID приоритета") UUID id) {
        taskPriorityService.delete(id);
    }
}


