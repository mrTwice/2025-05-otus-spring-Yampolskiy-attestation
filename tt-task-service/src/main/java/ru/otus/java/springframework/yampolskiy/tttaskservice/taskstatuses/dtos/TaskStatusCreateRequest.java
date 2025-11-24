package ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на создание статуса задачи")
public class TaskStatusCreateRequest {

    @NotBlank
    @Schema(description = "Код статуса", example = "in_progress")
    private String code;

    @NotBlank
    @Schema(description = "Название", example = "В работе")
    private String name;

    @Schema(description = "Описание", example = "Задача в процессе выполнения")
    private String description;

    @Schema(description = "Финальный статус", example = "false")
    private boolean isFinal;

    @Schema(description = "По умолчанию", example = "false")
    private boolean isDefault;

    @Schema(description = "Сортировка", example = "1")
    private Integer sortOrder;

    @Schema(description = "Цвет в HEX", example = "#33AACC")
    private String color;
}
