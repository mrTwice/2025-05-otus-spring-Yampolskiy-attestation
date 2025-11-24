package ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.error;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(name = "ValidationEmailPartErrorDTO", description = "Ошибка валидации конкретной части email-данных")
public class ValidationEmailPartErrorDTO {

    @Schema(description = "Поле с ошибкой", example = "email")
    private String field;

    @Schema(description = "Сообщение об ошибке", example = "Некорректный формат email")
    private String message;

    public ValidationEmailPartErrorDTO() {
    }

    public ValidationEmailPartErrorDTO(String field, String message) {
        this.field = field;
        this.message = message;
    }

}
