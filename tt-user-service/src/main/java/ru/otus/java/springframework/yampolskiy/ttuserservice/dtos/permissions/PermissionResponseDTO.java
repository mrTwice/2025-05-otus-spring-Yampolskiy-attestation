package ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.permissions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Ответ с данными разрешения")
public class PermissionResponseDTO {

    @Schema(description = "Идентификатор разрешения", example = "1f7e5e7a-ccf9-4c2c-9e4f-6c6f142a6e1a")
    private UUID id;

    @Schema(description = "Уникальное имя разрешения", example = "task:create")
    private String name;

    @Schema(description = "Описание разрешения", example = "Позволяет управлять задачами")
    private String description;

    @Schema(description = "Дата создания", example = "2024-04-01T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Дата последнего обновления", example = "2024-04-02T13:45:00")
    private LocalDateTime updatedAt;
}