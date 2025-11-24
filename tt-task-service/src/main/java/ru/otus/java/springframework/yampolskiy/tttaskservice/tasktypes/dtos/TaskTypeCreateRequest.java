package ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на создание типа задачи")
public class TaskTypeCreateRequest {

    @NotBlank
    @Schema(description = "Код типа задачи", example = "bug")
    private String code;

    @NotBlank
    @Schema(description = "Название типа задачи", example = "Баг")
    private String name;

    @Schema(description = "Описание", example = "Исправление дефекта")
    private String description;

    @Schema(description = "Тип задачи по умолчанию", example = "false")
    private boolean isDefault = false;

    @Schema(description = "Сортировка", example = "1")
    private Integer sortOrder;

    @Schema(description = "Иконка", example = "🐞")
    private String icon;
}
