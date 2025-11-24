package ru.otus.java.springframework.yampolskiy.tttaskservice.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.java.springframework.yampolskiy.tttaskservice.common.dto.ErrorDTO;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

@OpenAPIDefinition(
        info = @Info(
                title = "TT Task Service",
                version = "1.0",
                description = "Task API"
        ),
        security = @SecurityRequirement(name = "oidc")
)
@SecurityScheme(
        name = "oidc",
        type = SecuritySchemeType.OAUTH2,
        scheme = "bearer",
        bearerFormat = "JWT",
        flows = @OAuthFlows(
                authorizationCode = @OAuthFlow(
                        authorizationUrl = "/oauth2/authorize",
                        tokenUrl = "/oauth2/token",
                        scopes = {
                                @OAuthScope(name = "openid", description = "Базовая идентификация пользователя"),
                                @OAuthScope(name = "profile", description = "Профиль пользователя"),
                                @OAuthScope(name = "offline_access", description = "Оффлайн-доступ (refresh token)"),

                                @OAuthScope(name = "task:create", description = "Создание задач"),
                                @OAuthScope(name = "task:view", description = "Просмотр задач"),
                                @OAuthScope(name = "task:update", description = "Редактирование задач"),
                                @OAuthScope(name = "task:delete", description = "Удаление задач"),
                                @OAuthScope(name = "task:assign", description = "Назначение задач"),

                                @OAuthScope(name = "comment:create", description = "Создание комментариев"),
                                @OAuthScope(name = "comment:view", description = "Просмотр комментариев"),
                                @OAuthScope(name = "comment:update", description = "Редактирование комментариев"),
                                @OAuthScope(name = "comment:delete", description = "Удаление комментариев"),

                                @OAuthScope(name = "attachment:create", description = "Создание вложений"),
                                @OAuthScope(name = "attachment:view", description = "Просмотр вложений"),
                                @OAuthScope(name = "attachment:update", description = "Редактирование вложений"),
                                @OAuthScope(name = "attachment:delete", description = "Удаление вложений"),

                                @OAuthScope(name = "task-type:create", description = "Создание типов задач"),
                                @OAuthScope(name = "task-type:view", description = "Просмотр типов задач"),
                                @OAuthScope(name = "task-type:update", description = "Редактирование типов задач"),
                                @OAuthScope(name = "task-type:delete", description = "Удаление типов задач"),

                                @OAuthScope(name = "task-status:create", description = "Создание статусов задач"),
                                @OAuthScope(name = "task-status:view", description = "Просмотр статусов задач"),
                                @OAuthScope(name = "task-status:update", description = "Редактирование статусов задач"),
                                @OAuthScope(name = "task-status:delete", description = "Удаление статусов задач"),

                                @OAuthScope(name = "task-priority:create", description = "Создание приоритетов задач"),
                                @OAuthScope(name = "task-priority:view", description = "Просмотр приоритетов задач"),
                                @OAuthScope(
                                        name = "task-priority:update",
                                        description = "Редактирование приоритетов задач"
                                ),
                                @OAuthScope(name = "task-priority:delete", description = "Удаление приоритетов задач")
                        }

                )
        )
)
@Configuration
public class TaskServiceOpenApiConfig {

    @Bean
    public OpenApiCustomizer globalResponseCustomiser() {
        return openApi -> {
            if (openApi.getPaths() != null) {
                openApi.getPaths().forEach((path, pathItem) -> {
                    pathItem.readOperations().forEach(this::addGlobalResponses);
                });
            }
        };
    }

    private void addGlobalResponses(Operation operation) {
        ApiResponses responses = operation.getResponses();

        responses.addApiResponse(
                "400", createResponse(
                        "Bad Request (ошибка запроса)", "#/components/schemas/ErrorDTO"));
        responses.addApiResponse(
                "401", createResponse(
                        "Unauthorized (неавторизован)", "#/components/schemas/ErrorDTO"));
        responses.addApiResponse(
                "403", createResponse(
                        "Forbidden (нет доступа)", "#/components/schemas/ErrorDTO"));
        responses.addApiResponse(
                "404", createResponse(
                        "Not Found", "#/components/schemas/ErrorDTO"));
        responses.addApiResponse(
                "500", createResponse(
                        "Internal Server Error", "#/components/schemas/ErrorDTO"));
    }

    private ApiResponse createResponse(String description, String schemaRef) {
        return new ApiResponse()
                .description(description)
                .content(new Content().addMediaType("application/json",
                        new MediaType().schema(new Schema<>().$ref(schemaRef))));
    }

    @Bean
    public OpenAPI customOpenAPI(OpenApiProperties props) {
        return new OpenAPI()
                .servers(List.of(
                        new Server()
                                .url(props.getServerUrl())
                                .description("Gateway to Task Service")
                ))
                .components(new Components()
                        .addSchemas("ErrorDTO", new Schema<ErrorDTO>().$ref("#/components/schemas/ErrorDTO"))

                )
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("TT Task Service API")
                        .version("1.0")
                        .description("Документация сервиса задач")
                );
    }
}
