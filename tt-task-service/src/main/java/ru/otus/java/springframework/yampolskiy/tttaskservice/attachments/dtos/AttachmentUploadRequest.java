package ru.otus.java.springframework.yampolskiy.tttaskservice.attachments.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на загрузку метаинформации вложения")
public class AttachmentUploadRequest {

    @NotNull
    @Schema(description = "ID задачи", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    private UUID taskId;

    @NotBlank
    @Schema(description = "Имя файла", example = "design.pdf")
    private String fileName;

    @Schema(description = "Размер файла в байтах", example = "204800")
    private Long fileSize;

    @Schema(description = "MIME-тип файла", example = "application/pdf")
    private String contentType;
}
