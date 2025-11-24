package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.controllers.tasks;

import io.swagger.v3.oas.annotations.Operation;
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
import ru.otus.java.springframework.yampolskiy.tttaskservice.common.dto.ErrorDTO;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.task.TaskCreateRequest;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.task.TaskResponse;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.task.TaskUpdateRequest;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.task.TaskShortResponse;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.task.TaskStatusChangeRequest;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.task.TaskFilterRequest;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.entities.Task;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.mappers.TaskMapper;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.services.tasks.TaskService;

import java.util.List;
import java.util.UUID;

@Tag(name = "Tasks", description = "Управление задачами")
@SecurityRequirement(name = "oidc")
@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    @Operation(summary = "Создать задачу")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Задача успешно создана",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Неверный запрос",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Нет доступа",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "422",
                    description = "Ошибка валидации",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping
    @PreAuthorize("@taskAccessPolicy.canCreateTask(authentication)")
    public TaskResponse create(@RequestBody @Valid TaskCreateRequest request) {
        Task task = taskMapper.toEntity(request);
        Task created = taskService.create(task);
        return taskMapper.toResponse(created);
    }

    @Operation(summary = "Обновить задачу")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Задача обновлена",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @PutMapping("/{id}")
    @PreAuthorize("@taskAccessPolicy.canUpdateTask(authentication)")
    public TaskResponse update(@PathVariable UUID id, @RequestBody @Valid TaskUpdateRequest request) {
        Task existing = taskService.findById(id);
        taskMapper.updateEntity(existing, request);
        Task updated = taskService.update(id, existing);
        return taskMapper.toResponse(updated);
    }

    @Operation(summary = "Получить задачу по ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Задача найдена",
                    content = @Content(schema = @Schema(implementation = TaskResponse.class))),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @GetMapping("/{id}")
    @PreAuthorize("@taskAccessPolicy.canViewTask(authentication)")
    public TaskResponse getById(@PathVariable UUID id) {
        return taskMapper.toResponse(taskService.findById(id));
    }

    @Operation(summary = "Получить краткую информацию о задаче по ID")
    @ApiResponse(
            responseCode = "200",
            description = "Краткая информация",
            content = @Content(schema = @Schema(implementation = TaskShortResponse.class)))
    @GetMapping("/{id}/short")
    @PreAuthorize("@taskAccessPolicy.canViewTask(authentication)")
    public TaskShortResponse getShort(@PathVariable UUID id) {
        return taskMapper.toShortResponse(taskService.findById(id));
    }

    @Operation(summary = "Фильтрация задач (без пагинации)")
    @ApiResponse(
            responseCode = "200",
            description = "Результаты фильтрации",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskShortResponse.class))))
    @PostMapping("/filter")
    @PreAuthorize("@taskAccessPolicy.canViewTask(authentication)")
    public List<TaskShortResponse> filter(@RequestBody TaskFilterRequest filter) {
        return taskMapper.toShortResponseList(taskService.findAll(filter));
    }

    @Operation(summary = "Фильтрация задач с пагинацией")
    @ApiResponse(
            responseCode = "200",
            description = "Результаты фильтрации",
            content = @Content(schema = @Schema(implementation = TaskShortResponse.class)))
    @PostMapping("/filter/paged")
    @PreAuthorize("@taskAccessPolicy.canViewTask(authentication)")
    public Page<TaskShortResponse> filterPaged(@RequestBody TaskFilterRequest filter, Pageable pageable) {
        return taskService.findAll(filter, pageable).map(taskMapper::toShortResponse);
    }

    @Operation(summary = "Получить подзадачи")
    @ApiResponse(
            responseCode = "200",
            description = "Список подзадач",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskShortResponse.class))))
    @GetMapping("/subtasks/{parentId}")
    @PreAuthorize("@taskAccessPolicy.canViewTask(authentication)")
    public List<TaskShortResponse> getSubtasks(@PathVariable UUID parentId) {
        return taskMapper.toShortResponseList(taskService.findSubtasks(parentId));
    }

    @Operation(summary = "Получить задачи, назначенные на пользователя")
    @ApiResponse(
            responseCode = "200",
            description = "Список задач",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskShortResponse.class))))
    @GetMapping("/by-assignee/{userId}")
    @PreAuthorize("@taskAccessPolicy.canViewTask(authentication)")
    public List<TaskShortResponse> getByAssignee(@PathVariable UUID userId) {
        return taskMapper.toShortResponseList(taskService.findByAssignee(userId));
    }

    @Operation(summary = "Получить задачи, созданные пользователем")
    @ApiResponse(
            responseCode = "200",
            description = "Список задач",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskShortResponse.class))))
    @GetMapping("/by-creator/{userId}")
    @PreAuthorize("@taskAccessPolicy.canViewTask(authentication)")
    public List<TaskShortResponse> getByCreator(@PathVariable UUID userId) {
        return taskMapper.toShortResponseList(taskService.findByCreator(userId));
    }

    @Operation(summary = "Подсчитать количество задач, назначенных на пользователя")
    @ApiResponse(
            responseCode = "200",
            description = "Количество задач",
            content = @Content(schema = @Schema(implementation = Long.class)))
    @GetMapping("/count/by-assignee/{userId}")
    @PreAuthorize("@taskAccessPolicy.canViewTask(authentication)")
    public long countByAssignee(@PathVariable UUID userId) {
        return taskService.countByAssignee(userId);
    }

    @Operation(summary = "Сменить статус задачи")
    @ApiResponse(
            responseCode = "200",
            description = "Статус изменён",
            content = @Content(schema = @Schema(implementation = TaskResponse.class)))
    @PostMapping("/{id}/status")
    @PreAuthorize("@taskAccessPolicy.canUpdateTask(authentication) or @taskAccessPolicy.canAssignTask(authentication)")
    public TaskResponse changeStatus(@PathVariable UUID id, @RequestBody @Valid TaskStatusChangeRequest request) {
        Task existing = taskService.findById(id);
        existing.setStatusId(request.getStatusId());
        return taskMapper.toResponse(taskService.update(id, existing));
    }

    @Operation(summary = "Удалить задачу (мягкое удаление)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Удалено"),
            @ApiResponse(responseCode = "404", description = "Не найдено")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("@taskAccessPolicy.canDeleteTask(authentication)")
    public void delete(@PathVariable UUID id) {
        taskService.delete(id);
    }
}

