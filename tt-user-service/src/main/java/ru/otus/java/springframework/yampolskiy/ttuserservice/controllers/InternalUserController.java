package ru.otus.java.springframework.yampolskiy.ttuserservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.error.ErrorDTO;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.error.ValidationEmailErrorDTO;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.error.ValidationEmailPartErrorDTO;
import ru.otus.java.springframework.yampolskiy.ttuserservice.dtos.user.UserCreateDTO;
import ru.otus.java.springframework.yampolskiy.ttuserservice.entities.User;
import ru.otus.java.springframework.yampolskiy.ttuserservice.mappers.UserMapper;
import ru.otus.java.springframework.yampolskiy.ttuserservice.services.UserService;

@Slf4j
@RestController
@RequestMapping("/api/v1/internal/users")
@RequiredArgsConstructor
@Tag(name = "Internal Users", description = "Операции для внутренних клиентов (например, SAS)")
public class InternalUserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @Operation(
            summary = "Создать пользователя из SAS",
            description = "Доступно только для внутреннего клиента, проверка client_id = internal-service-client"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "403", description = "Нет доступа (не internal client)"),
            @ApiResponse(responseCode = "400", description = "Ошибка"),
            @ApiResponse(
                    responseCode = "400", description = "Ошибка",
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
    @PostMapping
    @PreAuthorize("@accessPolicy.isInternalClient(authentication)")
    public ResponseEntity<Void> createFromSas(
            @RequestBody @Valid @Parameter(description = "Данные нового пользователя") UserCreateDTO dto,
            @Parameter(hidden = true) Authentication authentication) {

        if (authentication instanceof OAuth2AuthenticatedPrincipal principal) {
            log.info("🐾 Principal claims: {}", principal.getAttributes());
        }

        User user = userMapper.toEntityFromUserCreateDTO(dto);
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
