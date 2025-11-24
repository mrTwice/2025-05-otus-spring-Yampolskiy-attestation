package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.taskpriority;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на создание приоритета задачи")
public class TaskPriorityCreateRequest {

    @NotBlank
    @Schema(description = "Уникальный код приоритета (неизменяемый)", example = "high")
    private String code;

    @NotBlank
    @Schema(description = "Отображаемое имя приоритета", example = "Высокий")
    private String name;

    @Schema(description = "Описание приоритета", example = "Приоритет для критичных задач")
    private String description;

    @Schema(description = "Порядок сортировки", example = "1")
    private Integer sortOrder;

    @Schema(description = "Цвет (HEX)", example = "#FF0000")
    private String color;

    @Schema(description = "Флаг приоритета по умолчанию", example = "true")
    private boolean isDefault;
}

