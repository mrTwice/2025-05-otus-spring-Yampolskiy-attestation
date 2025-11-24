package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.integrations.users.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.integrations.config.OAuth2AccessTokenManager;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.integrations.exceptions.IntegrationException;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.integrations.users.dto.UserPrincipalDTO;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserProfileClient {

    private static final String BASE_PATH = "/api/v1/users";

    private final RestClient userServiceClient;

    private final OAuth2AccessTokenManager userServiceAccessTokenManager;

    public UserPrincipalDTO findByUsername(String username) {
        return userServiceClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/profile")
                        .queryParam("username", username)
                        .build())
                .headers(headers -> {
                    String token = userServiceAccessTokenManager.getAccessToken();
                    log.debug("🔑 Access token for user-service (first 10 chars): {}",
                            token != null && token.length() > 10 ? token.substring(0, 10) : token);
                    headers.setBearerAuth(token);
                })
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals, processServiceUnauthorizedStatus(username))
                .onStatus(HttpStatus.NOT_FOUND::equals, processServiceNotFoundStatus(username))
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, processServiceInternalServerError(username))
                .body(UserPrincipalDTO.class);
    }

    private static RestClient.ResponseSpec.ErrorHandler processServiceInternalServerError(String username) {
        return (req, res) -> {
            log.error("Непредвиденная ошибка на стороне User-Service при поиске по username: {}", username);
            throw new IntegrationException("USER_SERVICE_UNAVAILABLE", "Ошибка сервиса пользователей");
        };
    }

    private static RestClient.ResponseSpec.ErrorHandler processServiceNotFoundStatus(String username) {
        return (req, res) -> {
            log.warn("User not found by username: {}", username);
            throw new IntegrationException("USER_NOT_FOUND", "Пользователь не найден");
        };
    }

    private static RestClient.ResponseSpec.ErrorHandler processServiceUnauthorizedStatus(String username) {
        return (req, res) -> {
            log.error("🚫 User-Service вернул 401 при поиске пользователя {}", username);
            throw new IntegrationException("USER_SERVICE_UNAUTHORIZED",
                    "User-Service вернул 401 Unauthorized");
        };
    }

    public UserPrincipalDTO findBySubject(UUID subject) {
        return userServiceClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/by-subject")
                        .queryParam("subject", subject)
                        .build())
                .headers(headers -> headers.setBearerAuth(userServiceAccessTokenManager.getAccessToken()))
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, (req, res) -> {
                    log.warn("User not found by subject: {}", subject);
                    throw new IntegrationException("USER_NOT_FOUND", "Пользователь не найден");
                })
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, (req, res) -> {
                    log.error("Непредвиденная ошибка на стороне User-Service при поиске по subject: {}", subject);
                    throw new IntegrationException("USER_SERVICE_UNAVAILABLE", "Ошибка сервиса пользователей");
                })
                .body(UserPrincipalDTO.class);
    }

    public boolean existsBySubject(UUID subject) {
        return Boolean.TRUE.equals(userServiceClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/exists")
                        .queryParam("subject", subject)
                        .build())
                .headers(headers -> headers.setBearerAuth(userServiceAccessTokenManager.getAccessToken()))
                .retrieve()
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, (req, res) -> {
                    log.error("Непредвиденная ошибка при проверке существования subject: {}", subject);
                    throw new IntegrationException("USER_SERVICE_UNAVAILABLE", "Ошибка сервиса пользователей");
                })
                .body(Boolean.class));
    }

    public List<UserPrincipalDTO> findByProvider(String provider) {
        return userServiceClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/by-provider")
                        .queryParam("provider", provider)
                        .build())
                .headers(headers -> headers.setBearerAuth(userServiceAccessTokenManager.getAccessToken()))
                .retrieve()
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, (req, res) -> {
                    log.error("Непредвиденная ошибка при получении пользователей по провайдеру: {}", provider);
                    throw new IntegrationException("USER_SERVICE_UNAVAILABLE", "Ошибка сервиса пользователей");
                })
                .body(new ParameterizedTypeReference<>() {});
    }

    public UserPrincipalDTO findBySubjectAndProvider(UUID subject, String provider) {
        return userServiceClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(BASE_PATH + "/auth/profile/oidc")
                        .queryParam("subject", subject)
                        .queryParam("provider", provider)
                        .build())
                .headers(headers -> headers.setBearerAuth(userServiceAccessTokenManager.getAccessToken()))
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, (req, res) -> {
                    log.warn("User not found by subject {} and provider {}", subject, provider);
                    throw new IntegrationException("USER_NOT_FOUND", "Пользователь не найден");
                })
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, (req, res) -> {
                    log.error("Ошибка при получении пользователя по subject и provider");
                    throw new IntegrationException("USER_SERVICE_UNAVAILABLE", "Ошибка сервиса пользователей");
                })
                .body(UserPrincipalDTO.class);
    }
}