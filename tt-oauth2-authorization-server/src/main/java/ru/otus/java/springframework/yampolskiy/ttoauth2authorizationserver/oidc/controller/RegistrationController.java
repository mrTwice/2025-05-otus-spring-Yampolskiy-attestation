package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.oidc.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.integrations.users.dto.UserCreateDTO;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.oidc.dto.UserRegistrationRequest;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.oidc.mapper.UserRegistrationMapper;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.oidc.service.UserRegistrationService;

@Tag(name = "Auth", description = "Регистрация и аутентификация пользователей")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class RegistrationController {

    private final UserRegistrationService userRegistrationService;

    private final UserRegistrationMapper userRegistrationMapper;

    @Operation(summary = "Регистрация нового пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации запроса")
    })
    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        UserCreateDTO userCreateDTO = userRegistrationMapper.toUserCreateDTO(request);
        userRegistrationService.registerLocalUser(userCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}