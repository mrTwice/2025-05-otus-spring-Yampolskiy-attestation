package ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO для административного обновления пользователя")
public class UserAdminUpdateDTO {

    @Schema(description = "Активен ли пользователь", example = "true")
    private boolean active;

    @Schema(description = "Подтвержден ли email", example = "true")
    private boolean emailVerified;

    @NotNull
    @Size(min = 1, message = "Необходимо указать хотя бы одну роль")
    @Schema(description = "Список ролей пользователя", example = "[\"USER\", \"MANAGER\"]")
    private Set<String> roles;
}
