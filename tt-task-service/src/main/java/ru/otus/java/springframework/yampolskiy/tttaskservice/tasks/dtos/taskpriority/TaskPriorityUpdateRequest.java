package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.taskpriority;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на обновление приоритета задачи")
public class TaskPriorityUpdateRequest {

    @NotBlank
    @Schema(description = "Название приоритета", example = "Средний")
    private String name;

    @Schema(description = "Описание", example = "Обновлённое описание приоритета")
    private String description;

    @Schema(description = "Порядок сортировки", example = "2")
    private Integer sortOrder;

    @Schema(description = "Цвет (HEX)", example = "#00AAFF")
    private String color;

    @Schema(description = "Флаг по умолчанию", example = "false")
    private boolean isDefault;
}

