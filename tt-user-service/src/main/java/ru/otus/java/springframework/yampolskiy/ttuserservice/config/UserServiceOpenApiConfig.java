package ru.otus.java.springframework.yampolskiy.ttuserservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@OpenAPIDefinition(
        info = @Info(
                title = "TT User Service",
                version = "1.0",
                description = "User API"
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
                                @OAuthScope(name = "openid", description = "Basic identity scope"),
                                @OAuthScope(name = "profile", description = "User profile"),
                                @OAuthScope(name = "offline_access", description = "Offline access (refresh tokens)"),
                                @OAuthScope(name = "user:view", description = "View users"),
                                @OAuthScope(name = "user:update", description = "Update users"),
                                @OAuthScope(name = "user:delete", description = "Delete users"),
                                @OAuthScope(name = "user:assign-roles", description = "Assign roles"),
                                @OAuthScope(name = "user:manage", description = "Manage users"),
                                @OAuthScope(name = "role:create", description = "Create roles"),
                                @OAuthScope(name = "role:view", description = "View roles"),
                                @OAuthScope(name = "role:update", description = "Update roles"),
                                @OAuthScope(name = "role:delete", description = "Delete roles"),
                                @OAuthScope(name = "permission:create", description = "Create permissions"),
                                @OAuthScope(name = "permission:view", description = "View permissions"),
                                @OAuthScope(name = "permission:update", description = "Update permissions"),
                                @OAuthScope(name = "permission:delete", description = "Delete permissions")
                        }
                )
        )
)
@Configuration
public class UserServiceOpenApiConfig {

    @Bean
    public OpenApiCustomizer globalResponseCustomiser(OpenApiProperties props) {
        return openApi -> {
            registerSchemas(openApi);
            configureMeta(openApi, props);
            addGlobalResponses(openApi);
        };
    }

    private void registerSchemas(OpenAPI openApi) {
        Components components = getOrCreateComponents(openApi);

        components
                .addSchemas("ErrorDTO", errorDtoSchema())
                .addSchemas("ValidationEmailPartErrorDTO", validationEmailPartErrorSchema())
                .addSchemas("ValidationEmailErrorDTO", validationEmailErrorSchema());
    }

    private Components getOrCreateComponents(OpenAPI openApi) {
        Components components = openApi.getComponents();
        if (components == null) {
            components = new Components();
            openApi.setComponents(components);
        }
        return components;
    }

    private Schema<?> errorDtoSchema() {
        return new Schema<>()
                .type("object")
                .addProperty("code", new StringSchema().description("Код ошибки"))
                .addProperty("message", new StringSchema().description("Сообщение ошибки"))
                .addProperty("dateTime", new StringSchema()
                        .format("date-time")
                        .description("Время ошибки"));
    }

    private Schema<?> validationEmailPartErrorSchema() {
        return new Schema<>()
                .type("object")
                .addProperty("field", new StringSchema().description("Поле с ошибкой"))
                .addProperty("message", new StringSchema().description("Сообщение об ошибке"));
    }

    private Schema<?> validationEmailErrorSchema() {
        return new Schema<>()
                .type("object")
                .addProperty("code", new StringSchema().description("Код ошибки"))
                .addProperty("message", new StringSchema().description("Общее сообщение"))
                .addProperty("errors", new ArraySchema()
                        .items(new Schema<>().$ref("#/components/schemas/ValidationEmailPartErrorDTO"))
                        .description("Список ошибок по полям"))
                .addProperty("dateTime", new StringSchema()
                        .format("date-time")
                        .description("Время ошибки"));
    }

    private void configureMeta(OpenAPI openApi, OpenApiProperties props) {
        openApi
                .servers(List.of(new Server()
                        .url(props.getServerUrl())
                        .description("Gateway")))
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("TT User Service API")
                        .version("1.0")
                        .description("Документация сервиса пользователей"));
    }

    private void addGlobalResponses(OpenAPI openApi) {
        if (openApi.getPaths() == null) {
            return;
        }

        openApi.getPaths().forEach((path, pathItem) ->
                pathItem.readOperations().forEach(operation -> {
                    ApiResponses responses = operation.getResponses();
                    processApiResponse(responses);
                })
        );
    }

    private static void processApiResponse(ApiResponses responses) {
        for (GlobalResponse r : GlobalResponse.values()) {
            responses.addApiResponse(
                    r.getCode(),
                    new ApiResponse()
                            .description(r.getDescription())
                            .content(new Content().addMediaType(
                                    "application/json",
                                    new MediaType().schema(new Schema<>().$ref(r.getSchema()))
                            ))
            );
        }
    }
}
