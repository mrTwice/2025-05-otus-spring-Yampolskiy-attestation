package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Краткая информация о задаче")
public class TaskShortResponse {

    @Schema(description = "ID задачи", example = "97c51cd2-e1c6-47f7-b730-3cdd30f1e2c2")
    private UUID id;

    @Schema(description = "Название задачи", example = "Обновить регистрацию")
    private String title;

    @Schema(description = "Код статуса", example = "in_progress")
    private String statusCode;

    @Schema(description = "Код типа задачи", example = "feature")
    private String typeCode;
}
