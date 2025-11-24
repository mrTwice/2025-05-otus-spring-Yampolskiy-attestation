package ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.permissions.PermissionDTO;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Ответ с данными роли")
public class RoleResponseDTO {

    @Schema(description = "ID роли", example = "cde6d9ab-0cb0-4be4-943a-b1e42fe0c02f")
    private UUID id;

    @Schema(description = "Имя роли", example = "MANAGER")
    private String name;

    @Schema(description = "Разрешения роли")
    private Set<PermissionDTO> permissions;

    @Schema(description = "Когда роль была создана", example = "2024-04-01T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Когда роль была обновлена", example = "2024-04-02T16:30:00")
    private LocalDateTime updatedAt;
}