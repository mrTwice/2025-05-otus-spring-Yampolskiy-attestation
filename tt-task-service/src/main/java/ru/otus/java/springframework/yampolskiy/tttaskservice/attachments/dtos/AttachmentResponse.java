package ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ с информацией о вложении")
public class AttachmentResponse {

    @Schema(description = "ID вложения", example = "e4eaaaf2-d142-11e1-b3e4-080027620cdd")
    private UUID id;

    @Schema(description = "ID задачи", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    private UUID taskId;

    @Schema(description = "Имя файла", example = "design.pdf")
    private String fileName;

    @Schema(description = "URL файла", example = "https://cdn.example.com/files/design.pdf")
    private String fileUrl;

    @Schema(description = "Размер файла", example = "204800")
    private Long fileSize;

    @Schema(description = "MIME-тип файла", example = "application/pdf")
    private String contentType;

    @Schema(description = "ID пользователя, загрузившего файл", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID uploadedBy;

    @Schema(description = "Дата и время загрузки", example = "2024-04-01T12:00:00Z")
    private Instant uploadedAt;
}

