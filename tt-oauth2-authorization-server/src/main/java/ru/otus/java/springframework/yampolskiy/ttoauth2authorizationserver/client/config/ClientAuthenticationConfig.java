package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.client.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.ClientSecretAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.core.service.DatabaseAuthorizationService;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.client.infrastructure.ClientAuthenticationLoggingProvider;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.client.infrastructure.LoggingAuthorizationCodeAuthenticationProvider;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.client.service.SecurityRegisteredClientRepository;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.oidc.infrastructrue.security.LoggingAuthorizationCodeRequestAuthenticationProvider;

import java.util.List;

@Slf4j
@Configuration
public class ClientAuthenticationConfig {

    @Autowired(required = false)
    private List<AuthenticationProvider> allProviders;

    @Bean
    public CommandLineRunner providers() {
        return args -> {
            log.info("Все провайдеры: {}", allProviders);
        };
    }

    @Bean
    public AuthenticationProvider clientAuthenticationProvider(
            SecurityRegisteredClientRepository registeredClientRepository,
            DatabaseAuthorizationService authorizationService,
            PasswordEncoder passwordEncoder
    ) {
        ClientSecretAuthenticationProvider baseProvider =
                new ClientSecretAuthenticationProvider(registeredClientRepository, authorizationService);
        baseProvider.setPasswordEncoder(passwordEncoder);

        return new ClientAuthenticationLoggingProvider(baseProvider);
    }

    @Bean
    public AuthenticationProvider authorizationCodeAuthenticationProvider(
            OAuth2AuthorizationService authorizationService,
            OAuth2TokenGenerator<?> tokenGenerator
    ) {
        var delegate = new OAuth2AuthorizationCodeAuthenticationProvider(authorizationService, tokenGenerator);
        return new LoggingAuthorizationCodeAuthenticationProvider(delegate);
    }

    @Bean
    public AuthenticationProvider authorizationCodeRequestAuthenticationProvider(
            RegisteredClientRepository registeredClientRepository,
            OAuth2AuthorizationService authorizationService,
            OAuth2AuthorizationConsentService authorizationConsentService
    ) {
        var delegate = new OAuth2AuthorizationCodeRequestAuthenticationProvider(
                registeredClientRepository,
                authorizationService,
                authorizationConsentService
        );
        return new LoggingAuthorizationCodeRequestAuthenticationProvider(delegate);
    }

    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService() {
        return new InMemoryOAuth2AuthorizationConsentService();
    }
}
