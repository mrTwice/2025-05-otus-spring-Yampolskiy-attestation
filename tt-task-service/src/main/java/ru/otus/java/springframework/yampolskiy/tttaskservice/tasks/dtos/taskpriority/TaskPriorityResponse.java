package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.taskpriority;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Подробная информация о приоритете задачи")
public class TaskPriorityResponse {

    @Schema(description = "Уникальный идентификатор", example = "e3a7787b-1234-4567-8901-abcdefabcdef")
    private UUID id;

    @Schema(description = "Код приоритета", example = "medium")
    private String code;

    @Schema(description = "Имя приоритета", example = "Средний")
    private String name;

    @Schema(description = "Описание", example = "Задачи средней важности")
    private String description;

    @Schema(description = "Сортировка", example = "2")
    private Integer sortOrder;

    @Schema(description = "Цвет", example = "#CCCC00")
    private String color;

    @Schema(description = "Признак приоритета по умолчанию", example = "false")
    private boolean isDefault;

    @Schema(description = "Дата создания", example = "2024-04-01T12:00:00Z")
    private Instant createdAt;

    @Schema(description = "Дата последнего обновления", example = "2024-04-02T12:00:00Z")
    private Instant updatedAt;
}

