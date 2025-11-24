package ru.otus.java.springframework.yampolskiy.tttaskservice.tasktypes.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Краткая информация о типе задачи")
public class TaskTypeShortResponse {

    @Schema(description = "ID", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    private UUID id;

    @Schema(description = "Код", example = "task")
    private String code;

    @Schema(description = "Название", example = "Обычная задача")
    private String name;
}

