package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.integrations.users.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.integrations.config.OAuth2AccessTokenManager;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.integrations.exceptions.IntegrationException;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.integrations.users.dto.UserCreateDTO;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.infrastructure.dto.ErrorDTO;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.infrastructure.dto.ValidationEmailErrorDTO;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationClient {

    private static final String BASE_PATH = "/api/v1/internal/users";

    private final RestClient userServiceClient;

    private final OAuth2AccessTokenManager userServiceAccessTokenManager;

    public void createUser(UserCreateDTO request) {
        userServiceClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH)
                        .build()
                )
                .headers(headers -> headers.setBearerAuth(userServiceAccessTokenManager.getAccessToken()))
                .body(request)
                .exchange((req, res) -> handleCreateUserResponse(res, request));
    }

    private Void handleCreateUserResponse(
            RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse response,
            UserCreateDTO request
    ) throws IOException {
        HttpStatusCode status = response.getStatusCode();

        if (status.is2xxSuccessful()) {
            return null;
        } else if (status.isSameCodeAs(HttpStatus.CONFLICT)) {
            processConflictStatus(response);
        } else if (status.isSameCodeAs(HttpStatus.UNPROCESSABLE_ENTITY)) {
            processUnprocessableEntityStatus(response);
        } else if (status.isSameCodeAs(HttpStatus.INTERNAL_SERVER_ERROR)) {
            processInternalServerErrorStatus(request);
        }

        logUnexpectedError(response, status);
        throw new IntegrationException("UNKNOWN_ERROR", "Неизвестная ошибка");
    }

    private void logUnexpectedError(
            RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse response,
            HttpStatusCode status
    ) {
        try {
            String raw = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
            log.error("Unexpected error from user-service: {} - {}", status, raw);
        } catch (IOException e) {
            log.error("Unexpected error from user-service: {} - <body read failed: {}>", status, e.getMessage());
        }
    }

    private void processInternalServerErrorStatus(UserCreateDTO request) {
        log.error("Internal error from user-service for user {}", request.getUsername());
        throw new IntegrationException("Ошибка сервиса пользователей", "USER_SERVICE_UNAVAILABLE");
    }

    private void processUnprocessableEntityStatus(
            RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse clientResponse
    ) {
        try {
            ValidationEmailErrorDTO error = clientResponse.bodyTo(ValidationEmailErrorDTO.class);
            String message = error.getErrors().stream()
                    .map(err -> err.getField() + ": " + err.getMessage())
                    .collect(Collectors.joining("; "));
            log.warn("Validation error: {}", message);
            throw new IntegrationException(message, error.getCode());
        } catch (RestClientException ex) {
            ErrorDTO error = clientResponse.bodyTo(ErrorDTO.class);
            log.warn("Validation error (simple): {}", error.getMessage());
            throw new IntegrationException(error.getMessage(), error.getCode());
        }
    }

    private void processConflictStatus(RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse clientResponse) {
        ErrorDTO error = clientResponse.bodyTo(ErrorDTO.class);
        log.warn("User already exists: [{}] {}", error.getCode(), error.getMessage());
        throw new IntegrationException(error.getMessage(), error.getCode());
    }
}
