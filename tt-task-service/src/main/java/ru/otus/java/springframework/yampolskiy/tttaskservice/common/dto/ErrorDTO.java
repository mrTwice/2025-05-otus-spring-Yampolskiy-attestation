package ru.otus.java.springframework.yampolskiy.tttaskservice.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Schema(name = "ErrorDTO", description = "Стандартная структура ошибки")
public class ErrorDTO {

    @Schema(description = "Код ошибки", example = "RESOURCE_NOT_FOUND")
    private String code;

    @Schema(description = "Сообщение ошибки", example = "Сущность с таким ID не найден")
    private String message;

    @Schema(description = "Время ошибки", example = "2024-04-01T12:00:00")
    private LocalDateTime dateTime;

    public ErrorDTO(String code, String message) {
        this.code = code;
        this.message = message;
        this.dateTime = LocalDateTime.now();
    }
}