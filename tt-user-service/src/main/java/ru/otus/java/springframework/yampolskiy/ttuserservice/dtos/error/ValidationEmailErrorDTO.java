package ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.error;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(name = "ValidationEmailErrorDTO", description = "Ошибка валидации email с детальной информацией по полям")
public class ValidationEmailErrorDTO {

    @Schema(description = "Код ошибки", example = "EMAIL_VALIDATION_FAILED")
    private String code;

    @Schema(description = "Общее сообщение об ошибке", example = "Некорректные данные email")
    private String message;

    @Schema(description = "Список ошибок по полям")
    private List<ValidationEmailPartErrorDTO> errors;

    @Schema(description = "Время ошибки", example = "2024-04-01T12:00:00")
    private LocalDateTime dateTime;

    public ValidationEmailErrorDTO(String code, String message, List<ValidationEmailPartErrorDTO> errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
        this.dateTime = LocalDateTime.now();
    }
}
