package ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.dtos.TaskTypeListResponse;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.dtos.TaskTypeShortResponse;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.dtos.TaskTypeResponse;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.dtos.TaskTypeUpdateRequest;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.dtos.TaskTypeCreateRequest;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.entities.TaskType;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.mappers.TaskTypeMapper;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.services.TaskTypeService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/task-types")
@RequiredArgsConstructor
@Validated
public class TaskTypeController {

    private final TaskTypeService taskTypeService;

    private final TaskTypeMapper taskTypeMapper;

    @Operation(
            summary = "Получить все типы задач",
            description = "Возвращает список всех доступных типов задач (отсортированных)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно получен список типов задач",
                    content = @Content(schema = @Schema(implementation = TaskTypeListResponse.class))),
            @ApiResponse(responseCode = "403", description = "Нет прав на просмотр")
    })
    @GetMapping
    @PreAuthorize("@taskTypeAccessPolicy.canViewTaskType(authentication)")
    public TaskTypeListResponse getAll() {
        List<TaskType> types = taskTypeService.findAllSorted();
        List<TaskTypeResponse> responses = taskTypeMapper.toResponseList(types);
        return new TaskTypeListResponse(responses, responses.size());
    }

    @Operation(
            summary = "Получить типы задач по умолчанию",
            description = "Возвращает только те типы задач, которые помечены как default")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешно",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = TaskTypeShortResponse.class))
                    )
            )
    })
    @GetMapping("/default")
    @PreAuthorize("@taskTypeAccessPolicy.canViewTaskType(authentication)")
    public List<TaskTypeShortResponse> getDefaultTypes() {
        return taskTypeMapper.toShortResponseList(taskTypeService.findDefaults());
    }

    @Operation(summary = "Получить тип задачи по ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Тип задачи найден",
                    content = @Content(schema = @Schema(implementation = TaskTypeResponse.class))),
            @ApiResponse(responseCode = "404", description = "Тип задачи не найден")
    })
    @GetMapping("/{id}")
    @PreAuthorize("@taskTypeAccessPolicy.canViewTaskType(authentication)")
    public TaskTypeResponse getById(
            @Parameter(description = "ID типа задачи") @PathVariable UUID id
    ) {
        TaskType type = taskTypeService.findById(id);
        return taskTypeMapper.toResponse(type);
    }

    @Operation(summary = "Получить тип задачи по коду")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Тип задачи найден",
                    content = @Content(schema = @Schema(implementation = TaskTypeResponse.class))),
            @ApiResponse(responseCode = "404", description = "Тип задачи не найден")
    })
    @GetMapping("/by-code/{code}")
    @PreAuthorize("@taskTypeAccessPolicy.canViewTaskType(authentication)")
    public TaskTypeResponse getByCode(
            @Parameter(description = "Код типа задачи") @PathVariable String code
    ) {
        TaskType type = taskTypeService.findByCode(code);
        return taskTypeMapper.toResponse(type);
    }

    @Operation(summary = "Поиск типов задач", description = "Поиск по названию или описанию типа задачи")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешно",
                    content = @Content(schema = @Schema(implementation = TaskTypeResponse.class)))
    })
    @GetMapping("/search")
    @PreAuthorize("@taskTypeAccessPolicy.canViewTaskType(authentication)")
    public Page<TaskTypeResponse> search(
            @Parameter(description = "Поисковая строка") @RequestParam String q,
            Pageable pageable
    ) {
        return taskTypeService.search(q, pageable).map(taskTypeMapper::toResponse);
    }

    @Operation(summary = "Создать тип задачи")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Тип задачи создан",
                    content = @Content(schema = @Schema(implementation = TaskTypeResponse.class))),
            @ApiResponse(responseCode = "422", description = "Ошибка валидации данных")
    })
    @PostMapping
    @PreAuthorize("@taskTypeAccessPolicy.canCreateTaskType(authentication)")
    public TaskTypeResponse create(
            @RequestBody @Valid TaskTypeCreateRequest request
    ) {
        TaskType entity = taskTypeMapper.toEntity(request);
        TaskType created = taskTypeService.create(entity);
        return taskTypeMapper.toResponse(created);
    }

    @Operation(summary = "Обновить тип задачи")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Тип задачи обновлён",
                    content = @Content(schema = @Schema(implementation = TaskTypeResponse.class))),
            @ApiResponse(responseCode = "404", description = "Тип задачи не найден")
    })
    @PutMapping("/{id}")
    @PreAuthorize("@taskTypeAccessPolicy.canUpdateTaskType(authentication)")
    public TaskTypeResponse update(
            @Parameter(description = "ID типа задачи") @PathVariable UUID id,
            @RequestBody @Valid TaskTypeUpdateRequest request
    ) {
        TaskType existing = taskTypeService.findById(id);
        taskTypeMapper.updateEntity(existing, request);
        TaskType updated = taskTypeService.update(id, existing);
        return taskTypeMapper.toResponse(updated);
    }

    @Operation(summary = "Удалить тип задачи (мягкое удаление)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Тип задачи удалён"),
            @ApiResponse(responseCode = "404", description = "Тип задачи не найден"),
            @ApiResponse(responseCode = "409", description = "Тип задачи уже удалён")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("@taskTypeAccessPolicy.canDeleteTaskType(authentication)")
    public void delete(
            @Parameter(description = "ID типа задачи") @PathVariable UUID id
    ) {
        taskTypeService.delete(id);
    }
}
