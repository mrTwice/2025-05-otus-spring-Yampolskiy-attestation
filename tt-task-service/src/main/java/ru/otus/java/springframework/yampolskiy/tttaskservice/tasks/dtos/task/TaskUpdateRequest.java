package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на обновление задачи")
public class TaskUpdateRequest {

    @NotBlank
    @Schema(description = "Название задачи", example = "Обновить регистрацию")
    private String title;

    @Schema(description = "Описание задачи", example = "Добавить подтверждение email")
    private String description;

    @NotNull
    @Schema(description = "ID типа задачи", example = "7f8928ba-8c02-4de5-95bd-cf894d295a18")
    private UUID typeId;

    @NotNull
    @Schema(description = "ID статуса задачи", example = "2e5e63f7-6b64-48dc-82ce-37d6c2b14e34")
    private UUID statusId;

    @Schema(description = "ID исполнителя", example = "8b62286a-d93d-4f9a-84e6-7f7e0b314937")
    private UUID assigneeId;

    @Schema(description = "Дедлайн задачи", example = "2025-05-01T18:00:00Z")
    private Instant dueDate;

    @Schema(description = "Дата завершения задачи", example = "2025-04-20T18:00:00Z")
    private Instant completedAt;

    @Schema(description = "ID родительской задачи", example = "36b40c3f-b3a7-4c2a-8f32-39430293c1f3")
    private UUID parentId;
}

