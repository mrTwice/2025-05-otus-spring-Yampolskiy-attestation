package ru.otus.java.springframework.yampolskiy.tttaskservice.storage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@Schema(description = "Ответ с пресайн-ссылкой для загрузки и финальным URL файла")
public class PresignedUploadResponse {

    @Schema(
            description = "Пресайн-ссылка для загрузки файла (PUT)",
            example = "https://s3.example.com/bucket/filename?signature=abc123"
    )
    private String presignedUrl;

    @Schema(description = "URL для доступа к файлу после загрузки", example = "https://cdn.example.com/files/filename")
    private String fileUrl;
}

