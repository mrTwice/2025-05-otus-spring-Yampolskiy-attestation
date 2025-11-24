package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на создание новой задачи")
public class TaskCreateRequest {

    @NotBlank
    @Schema(description = "Название задачи", example = "Реализовать регистрацию")
    private String title;

    @Schema(description = "Описание задачи", example = "Форма регистрации с валидацией email")
    private String description;

    @Schema(description = "ID типа задачи (опционально)", example = "7f8928ba-8c02-4de5-95bd-cf894d295a18")
    private UUID typeId;

    @Schema(description = "ID статуса задачи (опционально)", example = "2e5e63f7-6b64-48dc-82ce-37d6c2b14e34")
    private UUID statusId;

    @Schema(description = "ID приоритета задачи (опционально)", example = "9f0c9f77-8233-4d0f-9b13-9e5ed4b2f61f")
    private UUID priorityId;

    @NotNull
    @Schema(description = "ID создателя", example = "d39357f7-d17f-4c9a-9316-7646b34c62b3")
    private UUID creatorId;

    @Schema(description = "ID исполнителя", example = "8b62286a-d93d-4f9a-84e6-7f7e0b314937")
    private UUID assigneeId;

    @Schema(description = "Дедлайн задачи", example = "2025-05-01T18:00:00Z")
    private Instant dueDate;

    @Schema(description = "ID родительской задачи", example = "36b40c3f-b3a7-4c2a-8f32-39430293c1f3")
    private UUID parentId;
}

