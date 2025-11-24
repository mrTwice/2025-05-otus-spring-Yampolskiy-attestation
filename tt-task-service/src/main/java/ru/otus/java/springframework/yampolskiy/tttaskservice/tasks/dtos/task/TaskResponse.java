package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.entities.Task;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Полная информация о задаче")
public class TaskResponse {

    @Schema(description = "ID задачи", example = "e3a7787b-1234-4567-8901-abcdefabcdef")
    private UUID id;

    @Schema(description = "Название задачи", example = "Реализовать регистрацию")
    private String title;

    @Schema(description = "Описание задачи", example = "Форма регистрации с валидацией email")
    private String description;

    @Schema(description = "ID типа", example = "f184a7be-1c5d-4975-b605-fef9d390fefe")
    private UUID typeId;

    @Schema(description = "Код типа", example = "feature")
    private String typeCode;

    @Schema(description = "ID статуса", example = "a0e1cc45-06a7-4f4c-97b6-36f0e2ea9429")
    private UUID statusId;

    @Schema(description = "Код статуса", example = "in_progress")
    private String statusCode;

    @Schema(description = "ID приоритета", example = "b7f4b3f3-557d-43f6-8853-87d2bfe09e61")
    private UUID priorityId;

    @Schema(description = "Код приоритета", example = "high")
    private String priorityCode;

    @Schema(description = "ID создателя", example = "d39357f7-d17f-4c9a-9316-7646b34c62b3")
    private UUID creatorId;

    @Schema(description = "ID исполнителя", example = "8b62286a-d93d-4f9a-84e6-7f7e0b314937")
    private UUID assigneeId;

    @Schema(description = "Дедлайн задачи", example = "2025-05-01T18:00:00Z")
    private Instant dueDate;

    @Schema(description = "Дата завершения", example = "2025-05-02T10:00:00Z")
    private Instant completedAt;

    @Schema(description = "Дата удаления", example = "2025-06-01T00:00:00Z")
    private Instant deletedAt;

    @Schema(description = "Дата создания", example = "2025-04-01T12:00:00Z")
    private Instant createdAt;

    @Schema(description = "Дата обновления", example = "2025-04-03T15:00:00Z")
    private Instant updatedAt;

    @Schema(description = "ID родительской задачи", example = "c3fbbf0c-9149-437a-b19e-6a5b41e68843")
    private UUID parentId;

    @Schema(description = "Список подзадач (вложенные задачи)")
    private List<Task> subtasks;
}

