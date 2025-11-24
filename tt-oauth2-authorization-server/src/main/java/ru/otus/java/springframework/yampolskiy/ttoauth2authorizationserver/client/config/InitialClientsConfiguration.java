package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.client.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.client.service.SecurityRegisteredClientRepository;

import java.util.UUID;

@Configuration
public class InitialClientsConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(InitialClientsConfiguration.class);

    @Bean
    @DependsOn("entityManagerFactory")
    public CommandLineRunner registerInternalClient(
            SecurityRegisteredClientRepository registeredClientRepository,
            PasswordEncoder passwordEncoder,
            TokenSettings tokenSettings
    ) {
        return args -> {
            if (registeredClientRepository.findByClientId("internal-service-client") == null) {
                LOGGER.info("🔐 Registering internal-service-client...");

                RegisteredClient internalClient = getInternalClient(passwordEncoder, tokenSettings);

                registeredClientRepository.save(internalClient);
                LOGGER.info("✅ internal-service-client registered");
            }
        };
    }

    private RegisteredClient getInternalClient(PasswordEncoder passwordEncoder, TokenSettings tokenSettings) {
        return RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("internal-service-client")
                .clientSecret(passwordEncoder.encode("internal-secret"))
                .clientAuthenticationMethods(authMethods -> {
                    authMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
                    authMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_POST);
                })
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scope("user:view")
                .scope("user:manage")
                .scope("user:delete")
                .tokenSettings(tokenSettings)
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .build())
                .build();
    }


    @Bean
    @DependsOn("entityManagerFactory")
    public CommandLineRunner registerSwaggerClientUserService(
            SecurityRegisteredClientRepository registeredClientRepository,
            PasswordEncoder passwordEncoder,
            TokenSettings tokenSettings,
            SwaggerUserServiceProperties swaggerUserServiceProperties
    ) {
        return args -> {
            String clientId = "swagger-client-user-service";
            if (registeredClientRepository.findByClientId(clientId) == null) {
                LOGGER.info("📚 Registering {}...", clientId);

                RegisteredClient client = getSwaggerClientUserService(
                        passwordEncoder,
                        tokenSettings,
                        clientId,
                        swaggerUserServiceProperties
                );

                registeredClientRepository.save(client);
                LOGGER.info("✅ {} registered successfully", clientId);
            }
        };
    }

    private RegisteredClient getSwaggerClientUserService(
            PasswordEncoder passwordEncoder,
            TokenSettings tokenSettings,
            String clientId,
            SwaggerUserServiceProperties swaggerUserServiceProperties
    ) {
        return RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(clientId)
                .clientSecret(passwordEncoder.encode("swagger-secret"))
                .clientAuthenticationMethods(authMethods -> {
                    authMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
                    authMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_POST);
                })
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri(swaggerUserServiceProperties.getRedirectUrl())
                .scope("openid").scope("profile").scope("offline_access").scope("user:view")
                .scope("user:update").scope("user:delete").scope("user:assign-roles").scope("user:manage")
                .scope("role:create").scope("role:view").scope("role:update").scope("role:delete")
                .scope("permission:create").scope("permission:view").scope("permission:update")
                .scope("permission:delete")
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .requireProofKey(false)
                        .build())
                .tokenSettings(tokenSettings)
                .build();
    }

    @Bean
    @DependsOn("entityManagerFactory")
    public CommandLineRunner registerSwaggerClientTaskService(
            SecurityRegisteredClientRepository registeredClientRepository,
            PasswordEncoder passwordEncoder,
            TokenSettings tokenSettings,
            SwaggerTaskServiceProperties swaggerTaskServiceProperties
    ) {
        return args -> {
            String clientId = "swagger-client-task-service";
            if (registeredClientRepository.findByClientId(clientId) == null) {
                LOGGER.info("📚 Registering {}...", clientId);

                RegisteredClient client = getSwaggerClientTaskService(
                        passwordEncoder,
                        tokenSettings,
                        clientId,
                        swaggerTaskServiceProperties
                );

                registeredClientRepository.save(client);
                LOGGER.info("✅ {} registered successfully", clientId);
            }
        };
    }

    private RegisteredClient getSwaggerClientTaskService(
            PasswordEncoder passwordEncoder,
            TokenSettings tokenSettings,
            String clientId,
            SwaggerTaskServiceProperties swaggerTaskServiceProperties
    ) {
        return RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(clientId)
                .clientSecret(passwordEncoder.encode("swagger-secret"))
                .clientAuthenticationMethods(authMethods -> {
                    authMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
                    authMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_POST);
                })
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri(swaggerTaskServiceProperties.getRedirectUrl())
                .scope("openid").scope("profile")
                .scope("offline_access").scope("task:create").scope("task:view").scope("task:update")
                .scope("task:delete").scope("task:assign").scope("comment:create")
                .scope("comment:view").scope("comment:update").scope("comment:delete").scope("attachment:create")
                .scope("attachment:view").scope("attachment:update").scope("attachment:delete")
                .scope("task-type:create").scope("task-type:view").scope("task-type:update")
                .scope("task-type:delete").scope("task-status:create").scope("task-status:view")
                .scope("task-status:update").scope("task-status:delete").scope("task-priority:create")
                .scope("task-priority:view").scope("task-priority:update").scope("task-priority:delete")
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false).requireProofKey(false).build())
                .tokenSettings(tokenSettings).build();
    }
}

