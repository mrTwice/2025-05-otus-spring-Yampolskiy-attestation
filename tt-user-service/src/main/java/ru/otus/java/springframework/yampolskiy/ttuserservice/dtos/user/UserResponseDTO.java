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
@Schema(description = "DTO для возврата информации о пользователе в ответах")
public class UserResponseDTO {

    @Schema(description = "Идентификатор пользователя", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Имя пользователя", example = "john.doe")
    private String username;

    @Schema(description = "Email", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Подтверждён ли email", example = "true")
    private boolean emailVerified;

    @Schema(description = "Имя", example = "John")
    private String firstName;

    @Schema(description = "Отчество", example = "Edward")
    private String middleName;

    @Schema(description = "Фамилия", example = "Doe")
    private String lastName;

    @Schema(description = "Никнейм", example = "johnd")
    private String nickname;

    @Schema(description = "Ссылка на фото", example = "https://example.com/photo.jpg")
    private String pictureUrl;

    @Schema(description = "Профиль", example = "https://example.com/profile/johnd")
    private String profile;

    @Schema(description = "Вебсайт", example = "https://johndoe.dev")
    private String website;

    @Schema(description = "Локаль", example = "ru-RU")
    private String locale;

    @Schema(description = "Пол", example = "female")
    private String gender;

    @Schema(description = "Дата рождения", example = "1990-01-01")
    private LocalDate birthdate;

    @Schema(description = "Часовой пояс", example = "Asia/Yekaterinburg")
    private String zoneinfo;

    @Schema(description = "Номер телефона", example = "+79991234567")
    private String phoneNumber;

    @Schema(description = "Подтвержден ли номер", example = "true")
    private boolean phoneNumberVerified;

    @Schema(description = "Дата последнего обновления от OIDC", example = "2025-02-01T12:00:00Z")
    private Instant updatedAtOidc;

    @Schema(description = "Адрес пользователя", example = "{\"country\":\"RU\",\"city\":\"Moscow\"}")
    private Map<String, Object> address;

    @Schema(description = "Активен ли пользователь", example = "true")
    private boolean active;

    @Schema(description = "Заблокирован ли аккаунт", example = "false")
    private boolean locked;

    @Schema(description = "Когда истекают учетные данные", example = "2026-01-01T00:00:00Z")
    private Instant credentialsExpireAt;

    @Schema(description = "Когда истекает аккаунт", example = "2027-01-01T00:00:00Z")
    private Instant accountExpireAt;

    @Schema(description = "Роли пользователя", example = "[\"USER\"]")
    private Set<String> roles;
}
