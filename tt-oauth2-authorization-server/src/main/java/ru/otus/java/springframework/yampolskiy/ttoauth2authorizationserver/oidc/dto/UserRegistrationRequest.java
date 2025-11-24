package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.oidc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Запрос на регистрацию нового пользователя")
public class UserRegistrationRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    @Schema(description = "Уникальное имя пользователя (логин)", example = "john_doe")
    private String username;

    @NotBlank
    @Email
    @Schema(description = "Email адрес пользователя", example = "john.doe@example.com")
    private String email;

    @NotBlank
    @Size(min = 8, message = "Пароль должен быть не короче 8 символов")
    @Schema(description = "Пароль (не менее 8 символов)", example = "P@ssw0rd123")
    private String password;

    @Schema(description = "Имя", example = "Иван")
    private String firstName;

    @Schema(description = "Отчество", example = "Иванович")
    private String middleName;

    @Schema(description = "Фамилия", example = "Иванов")
    private String lastName;

    @Schema(description = "Псевдоним / никнейм", example = "ivan_the_great")
    private String nickname;

    @Schema(description = "URL аватарки", example = "https://example.com/avatar.jpg")
    private String pictureUrl;

    @Schema(description = "Профиль пользователя", example = "Frontend developer from Moscow")
    private String profile;

    @Schema(description = "Сайт пользователя", example = "https://example.com")
    private String website;

    @Schema(description = "Язык", example = "ru-RU")
    private String locale;

    @Schema(description = "Пол", example = "male")
    private String gender;

    @Schema(description = "Дата рождения", example = "1990-05-10")
    private LocalDate birthdate;

    @Schema(description = "Часовой пояс", example = "Europe/Moscow")
    private String zoneinfo;

    @Schema(description = "Номер телефона", example = "+79991234567")
    private String phoneNumber;

    @Schema(
            description = "Адрес пользователя в виде произвольного JSON",
            example = "{\"street\": \"Main St\", \"city\": \"Moscow\"}")
    private Map<String, Object> address; //TODO добавить отдельную сущность для адреса
}
