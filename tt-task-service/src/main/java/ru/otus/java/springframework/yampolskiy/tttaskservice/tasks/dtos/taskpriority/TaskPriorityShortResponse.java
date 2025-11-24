package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.taskpriority;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Краткая информация о приоритете задачи")
public class TaskPriorityShortResponse {

    @Schema(description = "ID", example = "1f4e5e0c-59db-4b0d-9cdd-e4ec73fdc53b")
    private UUID id;

    @Schema(description = "Код приоритета", example = "low")
    private String code;

    @Schema(description = "Имя приоритета", example = "Низкий")
    private String name;
}

