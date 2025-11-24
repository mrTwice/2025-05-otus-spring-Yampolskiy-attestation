package ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO, представляющее пользователя для целей аутентификации и авторизации")
public class UserPrincipalDTO {

    @Schema(description = "Уникальный идентификатор пользователя", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Уникальное имя пользователя (username)", example = "john.doe")
    private String username;

    @Schema(description = "Email пользователя", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Хеш пароля", example = "$2a$12$...")
    private String password;

    @Schema(description = "Подтверждён ли email", example = "true")
    private boolean emailVerified;

    @Schema(description = "Активен ли пользователь", example = "true")
    private boolean active;

    @Schema(description = "Заблокирован ли аккаунт", example = "false")
    private boolean locked;

    @Schema(description = "Время истечения срока действия учётных данных", example = "2025-12-31T23:59:59Z")
    private Instant credentialsExpireAt;

    @Schema(description = "Время истечения срока действия аккаунта", example = "2026-12-31T23:59:59Z")
    private Instant accountExpireAt;

    @Schema(description = "Имя", example = "John")
    private String firstName;

    @Schema(description = "Отчество", example = "Edward")
    private String middleName;

    @Schema(description = "Фамилия", example = "Doe")
    private String lastName;

    @Schema(description = "Никнейм пользователя", example = "johnd")
    private String nickname;

    @Schema(description = "Ссылка на фото пользователя", example = "https://example.com/images/avatar.jpg")
    private String pictureUrl;

    @Schema(description = "Профиль пользователя", example = "https://example.com/profiles/john")
    private String profile;

    @Schema(description = "Персональный сайт пользователя", example = "https://johndoe.dev")
    private String website;

    @Schema(description = "Язык/локаль пользователя", example = "en-US")
    private String locale;

    @Schema(description = "Пол", example = "male")
    private String gender;

    @Schema(description = "Дата рождения", example = "1990-01-01")
    private LocalDate birthdate;

    @Schema(description = "Часовой пояс", example = "Europe/Moscow")
    private String zoneinfo;

    @Schema(description = "Номер телефона", example = "+1234567890")
    private String phoneNumber;

    @Schema(description = "Подтвержден ли номер телефона", example = "false")
    private boolean phoneNumberVerified;

    @Schema(description = "OIDC subject (уникальный идентификатор от провайдера)", example = "abc123-oidc-sub")
    private String oidcSubject;

    @Schema(description = "OIDC провайдер", example = "google")
    private String oidcProvider;

    @Schema(description = "Дата последнего обновления через OIDC", example = "2025-01-01T12:00:00Z")
    private Instant updatedAtOidc;

    @Schema(description = "Адрес пользователя в виде произвольных пар ключ-значение")
    private Map<String, Object> address;

    @Schema(description = "Названия ролей, назначенных пользователю", example = "[\"USER\", \"ADMIN\"]")
    private Set<String> roles;

    @Schema(description = "Список пермишенов пользователя", example = "[\"TASK_VIEW\", \"TASK_EDIT\"]")
    private Set<String> permissions;
}
