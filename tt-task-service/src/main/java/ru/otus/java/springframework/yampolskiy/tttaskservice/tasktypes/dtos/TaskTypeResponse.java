package ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Тип задачи (подробно)")
public class TaskTypeResponse {

    @Schema(description = "ID", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    private UUID id;

    @Schema(description = "Код", example = "feature")
    private String code;

    @Schema(description = "Название", example = "Фича")
    private String name;

    @Schema(description = "Описание", example = "Добавление новой функциональности")
    private String description;

    @Schema(description = "По умолчанию", example = "false")
    private boolean isDefault;

    @Schema(description = "Порядок сортировки", example = "3")
    private Integer sortOrder;

    @Schema(description = "Иконка", example = "✨")
    private String icon;

    @Schema(description = "Дата создания", example = "2024-04-01T12:00:00Z")
    private Instant createdAt;

    @Schema(description = "Дата обновления", example = "2024-04-02T12:00:00Z")
    private Instant updatedAt;
}

