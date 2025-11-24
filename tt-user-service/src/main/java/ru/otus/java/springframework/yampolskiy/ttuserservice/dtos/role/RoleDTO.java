package ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.permissions.PermissionDTO;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для создания или обновления роли")
public class RoleDTO {

    @NotBlank
    @Schema(description = "Имя роли", example = "MANAGER")
    private String name;

    @NotNull
    @Size(min = 1, message = "Роль должна содержать хотя бы одно разрешение")
    @Schema(description = "Набор разрешений, связанных с ролью")
    private Set<@Valid PermissionDTO> permissions;
}
