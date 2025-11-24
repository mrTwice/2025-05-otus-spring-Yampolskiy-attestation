package ru.otus.java.springframework.yampolskiy.tttaskservice.storage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.java.springframework.yampolskiy.tttaskservice.storage.dto.PresignedUploadResponse;
import ru.otus.java.springframework.yampolskiy.tttaskservice.storage.service.FileStorageClient;

@RestController
@RequestMapping("/api/v1/storage")
@RequiredArgsConstructor
@Validated
@Tag(name = "Файловое хранилище", description = "Получение ссылок для загрузки файлов")
public class FileStorageController {

    private final FileStorageClient fileStorageClient;

    @GetMapping("/upload-url")
    @PreAuthorize("@storageAccessPolicy.hasPermission(authentication, 'attachment:create')")
    @Operation(
            summary = "Получить пресайн-ссылку для загрузки файла",
            description = "Генерирует временную ссылку для загрузки файла и возвращает URL для последующего доступа.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ссылки успешно сгенерированы",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PresignedUploadResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Нет прав на загрузку вложений"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректный запрос"
                    )
            }
    )
    public PresignedUploadResponse getPresignedUploadUrl(
            @Parameter(description = "Имя файла, включая расширение", example = "document.pdf", required = true)
            @RequestParam String fileName) {
        return fileStorageClient.generateUploadUrl(fileName);
    }
}

