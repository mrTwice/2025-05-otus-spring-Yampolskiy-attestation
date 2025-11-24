package ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для обновления публичных данных пользователя")
public class UserUpdateDTO {

    @NotBlank
    @Size(min = 3, max = 50)
    @Schema(description = "Имя пользователя", example = "john.doe")
    private String username;

    @NotBlank
    @Email
    @Schema(description = "Email", example = "john.doe@example.com")
    private String email;

    @Size(max = 100)
    @Schema(description = "Имя", example = "John")
    private String firstName;

    @Size(max = 100)
    @Schema(description = "Фамилия", example = "Doe")
    private String lastName;

    @Pattern(regexp = "https?://.*", message = "Некорректный URL")
    @Schema(description = "Ссылка на фото", example = "https://example.com/images/avatar.jpg")
    private String pictureUrl;

    @Size(max = 10)
    @Schema(description = "Язык/локаль", example = "en-US")
    private String locale;
}


