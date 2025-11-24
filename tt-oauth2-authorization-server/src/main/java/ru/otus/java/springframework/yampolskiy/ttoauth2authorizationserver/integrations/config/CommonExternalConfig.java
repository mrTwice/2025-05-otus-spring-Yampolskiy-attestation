package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.integrations.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.integrations.users.config.UserServiceProperties;

@Configuration
@EnableConfigurationProperties(UserServiceProperties.class)
public class CommonExternalConfig {

    @Bean(name = "sasTokenClient")
    public RestClient sasTokenClient(@Value("${auth-server.base-uri}") String baseUri) {
        return RestClient.builder()
                .baseUrl(baseUri)
                .build();
    }

    @Bean(name = "userServiceAccessTokenManager")
    public OAuth2AccessTokenManager userServiceAccessTokenManager(
            RestClient sasTokenClient,
            UserServiceProperties props
    ) {
        return new OAuth2AccessTokenManager(
                sasTokenClient,
                props.getClientId(),
                props.getClientSecret(),
                String.join(" ", props.getScopes())
        );
    }
}
