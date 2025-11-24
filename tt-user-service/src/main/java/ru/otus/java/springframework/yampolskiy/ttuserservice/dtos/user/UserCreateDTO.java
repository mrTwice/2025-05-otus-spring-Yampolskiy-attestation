package ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.user;

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
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Модель создания пользователя")
public class UserCreateDTO {

    @Schema(hidden = true)
    private UUID id;

    @NotBlank
    @Size(min = 3, max = 50)
    @Schema(description = "Имя пользователя", example = "johndoe")
    private String username;

    @NotBlank
    @Schema(description = "Пароль пользователя", example = "P@ssw0rd123")
    private String password;

    @NotBlank
    @Email
    @Schema(description = "Email пользователя", example = "john.doe@example.com")
    private String email;

    @NotBlank
    @Schema(description = "Провайдер OpenID Connect", example = "google")
    private String oidcProvider;

    @NotBlank
    @Schema(description = "OIDC subject", example = "abc123xyz")
    private String oidcSubject;

    @Schema(description = "Email подтвержден", example = "true")
    private boolean emailVerified;

    @Schema(description = "Пользователь активен", example = "true")
    private boolean active;

    @Schema(description = "Аккаунт заблокирован", example = "false")
    private boolean locked;

    @Schema(description = "Телефон подтвержден", example = "false")
    private boolean phoneNumberVerified;

    @Size(max = 100)
    @Schema(description = "Имя", example = "John")
    private String firstName;

    @Size(max = 100)
    @Schema(description = "Отчество", example = "Ivanovich")
    private String middleName;

    @Size(max = 100)
    @Schema(description = "Фамилия", example = "Doe")
    private String lastName;

    @Size(max = 100)
    @Schema(description = "Никнейм", example = "johnny")
    private String nickname;

    @Pattern(regexp = "https?://.*", message = "Должен быть валидным URL")
    @Schema(description = "URL фотографии", example = "https://example.com/avatar.jpg")
    private String pictureUrl;

    @Pattern(regexp = "https?://.*", message = "Должен быть валидным URL")
    @Schema(description = "Профиль", example = "https://example.com/profile")
    private String profile;

    @Pattern(regexp = "https?://.*", message = "Должен быть валидным URL")
    @Schema(description = "Сайт пользователя", example = "https://example.com")
    private String website;

    @Size(max = 10)
    @Schema(description = "Локаль пользователя", example = "ru-RU")
    private String locale;

    @Pattern(regexp = "male|female|other", message = "Допустимые значения: male, female, other")
    @Schema(description = "Пол", example = "male")
    private String gender;

    @Schema(description = "Дата рождения", example = "1990-01-01", format = "date")
    private LocalDate birthdate;

    @Size(max = 50)
    @Schema(description = "Часовой пояс", example = "Europe/Moscow")
    private String zoneinfo;

    @Pattern(regexp = "\\+?\\d{10,15}", message = "Неверный формат номера телефона")
    @Schema(description = "Номер телефона", example = "+79991234567")
    private String phoneNumber;

    @Schema(description = "Адрес пользователя в виде произвольного JSON")
    private Map<String, Object> address;
}