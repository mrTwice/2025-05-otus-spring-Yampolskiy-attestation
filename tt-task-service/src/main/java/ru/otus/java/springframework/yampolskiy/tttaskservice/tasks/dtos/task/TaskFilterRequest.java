package ru.otus.java.springframework.yampolskiy.tttaskservice.tasks.dtos.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Фильтрация задач по различным критериям")
public class TaskFilterRequest {

    @Schema(description = "Фильтрация по ID создателя", example = "d39357f7-d17f-4c9a-9316-7646b34c62b3")
    private UUID creatorId;

    @Schema(description = "Фильтрация по ID исполнителя", example = "8b62286a-d93d-4f9a-84e6-7f7e0b314937")
    private UUID assigneeId;

    @Schema(description = "Код статуса", example = "in_progress")
    private String statusCode;

    @Schema(description = "Код типа", example = "bug")
    private String typeCode;

    @Schema(description = "Фильтрация по дате создания: после", example = "2025-04-01T00:00:00Z")
    private Instant createdAfter;

    @Schema(description = "Фильтрация по дате создания: до", example = "2025-04-30T23:59:59Z")
    private Instant createdBefore;

    @Schema(description = "Фильтрация по приоритету", example = "a1b2c3d4-e5f6-7g8h-9i10-j11k12l13m14")
    private UUID priorityId;
}

