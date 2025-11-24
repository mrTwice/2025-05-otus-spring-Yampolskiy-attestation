package ru.otus.java.springframework.yampolskiy.tttaskservice.taskstatuses.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Краткое представление статуса")
public class TaskStatusShortResponse {

    @Schema(description = "ID", example = "b1a2c3d4-e5f6-7a89-b0c1-d2e3f4a5b6c7")
    private UUID id;

    @Schema(description = "Код", example = "done")
    private String code;

    @Schema(description = "Название", example = "Готово")
    private String name;

    @Schema(description = "Цвет", example = "#00FF00")
    private String color;
}

