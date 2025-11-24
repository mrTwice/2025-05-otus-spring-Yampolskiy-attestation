package ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ со статусом задачи")
public class TaskStatusResponse {

    @Schema(description = "ID", example = "b1a2c3d4-e5f6-7a89-b0c1-d2e3f4a5b6c7")
    private UUID id;

    @Schema(description = "Код", example = "done")
    private String code;

    @Schema(description = "Название", example = "Готово")
    private String name;

    @Schema(description = "Описание", example = "Задача завершена")
    private String description;

    @Schema(description = "Финальный статус", example = "true")
    private boolean isFinal;

    @Schema(description = "По умолчанию", example = "false")
    private boolean isDefault;

    @Schema(description = "Сортировка", example = "3")
    private Integer sortOrder;

    @Schema(description = "Цвет", example = "#00FF00")
    private String color;

    @Schema(description = "Дата создания", example = "2024-04-01T12:00:00Z")
    private Instant createdAt;

    @Schema(description = "Дата обновления", example = "2024-04-02T12:00:00Z")
    private Instant updatedAt;
}

