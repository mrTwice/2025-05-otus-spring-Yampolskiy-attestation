package ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на обновление статуса задачи")
public class TaskStatusUpdateRequest {

    @NotBlank
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
}
