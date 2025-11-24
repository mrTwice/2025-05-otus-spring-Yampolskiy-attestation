package ru.otus.java.springframework.yampolskiy.ttuserservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.error.ErrorDTO;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.error.ValidationEmailErrorDTO;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.error.ValidationEmailPartErrorDTO;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.user.UserCreateDTO;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.user.UserUpdateDTO;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.user.UserPrincipalDTO;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.user.UserAdminUpdateDTO;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.user.UserResponseDTO;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.user.PasswordHashUpdateDTO;
import ru.otus.java.springframework.yampolskiy.ttuserservice.entities.User;
import ru.otus.java.springframework.yampolskiy.ttuserservice.mappers.UserMapper;
import ru.otus.java.springframework.yampolskiy.ttuserservice.services.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "Операции с пользователями")
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @Operation(summary = "Получить пользователя по ID", description = "Возвращает информацию о пользователе по UUID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @GetMapping("/{id}")
    @PreAuthorize("@accessPolicy.canAccessUser(authentication, #id)")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable UUID id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(userMapper.toResponseDTOFromEntity(user));
    }

    @Operation(summary = "Обновить пользователя", description = "Обновляет публичные данные пользователя по UUID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь обновлён"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PutMapping("/{id}")
    @PreAuthorize("@accessPolicy.canUpdateUser(authentication, #id)")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id,
                                                      @RequestBody @Valid UserUpdateDTO dto) {
        User updated = userService.updateUser(id, userMapper.toEntityFromUserCreateDTO(
                UserCreateDTO.builder()
                        .username(dto.getUsername())
                        .email(dto.getEmail())
                        .firstName(dto.getFirstName())
                        .lastName(dto.getLastName())
                        .locale(dto.getLocale())
                        .pictureUrl(dto.getPictureUrl())
                        .password("placeholder")
                        .build()
        ));
        return ResponseEntity.ok(userMapper.toResponseDTOFromEntity(updated));
    }

    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Пользователь удалён"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("@accessPolicy.canDeleteUsers(authentication)")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Получить список активных пользователей",
            description = "Возвращает всех активных пользователей")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список пользователей"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(
                    responseCode = "422",
                    description = "Ошибка валидации email",
                    content = @Content(schema = @Schema(implementation = ValidationEmailErrorDTO.class))),
            @ApiResponse(
                    responseCode = "422",
                    description = "Ошибка валидации email",
                    content = @Content(schema = @Schema(implementation = ValidationEmailPartErrorDTO.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @GetMapping
    @PreAuthorize("@accessPolicy.canViewUsers(authentication)")
    public ResponseEntity<List<UserResponseDTO>> getAllActiveUsers() {
        List<User> users = userService.getAllActiveUsers();
        List<UserResponseDTO> dtos = users.stream()
                .map(userMapper::toResponseDTOFromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(
            summary = "Обновить хеш пароля",
            description = "Обновляет пароль пользователя (уже в захешированном виде)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Пароль обновлён"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Ошибка валидации",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PatchMapping("/{id}/password")
    @PreAuthorize("@accessPolicy.canChangePasswordFor(authentication, #id)")
    public ResponseEntity<Void> updatePasswordHash(@PathVariable UUID id,
                                                   @RequestBody @Valid PasswordHashUpdateDTO dto) {
        userService.updatePasswordHash(id, dto.getPasswordHash());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Административное обновление пользователя",
            description = "Позволяет администратору обновить статусы и роли пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь обновлён"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PatchMapping("/{id}/admin")
    @PreAuthorize("@accessPolicy.canAdminUpdateUsers(authentication)")
    public ResponseEntity<UserResponseDTO> adminUpdate(@PathVariable UUID id,
                                                       @RequestBody @Valid UserAdminUpdateDTO dto) {
        User user = userService.getUserById(id);
        userMapper.updateEntityFromAdminDTO(dto, user);
        User updated = userService.createUser(user);
        return ResponseEntity.ok(userMapper.toResponseDTOFromEntity(updated));
    }

    @Operation(summary = "Добавить роль пользователю", description = "Добавляет роль пользователю по имени")
    @ApiResponse(responseCode = "200", description = "Роль добавлена")
    @PatchMapping("/{id}/roles/add")
    @PreAuthorize("@accessPolicy.canAssignRoles(authentication)")
    public ResponseEntity<UserResponseDTO> addRole(@PathVariable UUID id,
                                                   @RequestParam String roleName) {
        User updated = userService.addRoleToUser(id, roleName);
        return ResponseEntity.ok(userMapper.toResponseDTOFromEntity(updated));
    }

    @Operation(summary = "Удалить роль у пользователя", description = "Удаляет указанную роль у пользователя")
    @ApiResponse(responseCode = "200", description = "Роль удалена")
    @PatchMapping("/{id}/roles/remove")
    @PreAuthorize("@accessPolicy.canAssignRoles(authentication)")
    public ResponseEntity<UserResponseDTO> removeRole(@PathVariable UUID id,
                                                      @RequestParam String roleName) {
        User updated = userService.removeRoleFromUser(id, roleName);
        return ResponseEntity.ok(userMapper.toResponseDTOFromEntity(updated));
    }

    @Operation(summary = "Получить пользователя по subject", description = "Поиск по уникальному subject OIDC")
    @ApiResponse(responseCode = "200", description = "Пользователь найден")
    @GetMapping("/by-subject")
    @PreAuthorize("@accessPolicy.canViewUsers(authentication)")
    public ResponseEntity<UserPrincipalDTO> getUserBySubject(@RequestParam String subject) {
        User user = userService.getUserByOidcSubject(subject);
        return ResponseEntity.ok(userMapper.toPrincipalDTO(user));
    }

    @Operation(
            summary = "Проверить существование пользователя",
            description = "Проверяет наличие пользователя по subject OIDC")
    @ApiResponse(responseCode = "200", description = "Флаг существования пользователя")
    @GetMapping("/exists")
    @PreAuthorize("@accessPolicy.canViewUsers(authentication)")
    public ResponseEntity<Boolean> exists(@RequestParam String subject) {
        return ResponseEntity.ok(userService.existsByOidcSubject(subject));
    }

    @Operation(
            summary = "Получить пользователей по провайдеру",
            description = "Возвращает пользователей по OIDC-провайдеру")
    @ApiResponse(responseCode = "200", description = "Список пользователей")
    @GetMapping("/by-provider")
    @PreAuthorize("@accessPolicy.canViewUsers(authentication)")
    public ResponseEntity<List<UserPrincipalDTO>> getUsersByProvider(@RequestParam String provider) {
        List<User> users = userService.getUsersByOidcProvider(provider);
        return ResponseEntity.ok(users.stream()
                .map(userMapper::toPrincipalDTO)
                .toList());
    }

    @Operation(
            summary = "Получить профиль пользователя по username",
            description = "Возвращает профиль по username (используется для авторизации)")
    @ApiResponse(responseCode = "200", description = "Профиль найден")
    @GetMapping("/profile")
    @PreAuthorize("@accessPolicy.canViewUsers(authentication)")
    public ResponseEntity<UserPrincipalDTO> getPrincipalByUsername(@RequestParam String username) {
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(userMapper.toPrincipalDTO(user));
    }

    @Operation(summary = "Получить профиль по subject и провайдеру", description = "OIDC-поиск по subject и провайдеру")
    @ApiResponse(responseCode = "200", description = "Профиль найден")
    @GetMapping("/auth/profile/oidc")
    @PreAuthorize("@accessPolicy.canViewUsers(authentication)")
    public ResponseEntity<UserPrincipalDTO> getPrincipalByOidcAndProvider(
            @RequestParam String provider,
            @RequestParam String subject
    ) {
        User user = userService.getUserByOidcSubjectAndProvider(subject, provider);
        return ResponseEntity.ok(userMapper.toPrincipalDTO(user));
    }
}

