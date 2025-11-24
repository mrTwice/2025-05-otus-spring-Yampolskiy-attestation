package ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на обновление типа задачи")
public class TaskTypeUpdateRequest {

    @NotBlank
    @Schema(description = "Название", example = "Новая задача")
    private String name;

    @Schema(description = "Описание", example = "Обновлённое описание")
    private String description;

    @Schema(description = "По умолчанию", example = "true")
    private boolean isDefault;

    @Schema(description = "Сортировка", example = "2")
    private Integer sortOrder;

    @Schema(description = "Иконка", example = "🛠")
    private String icon;
}
