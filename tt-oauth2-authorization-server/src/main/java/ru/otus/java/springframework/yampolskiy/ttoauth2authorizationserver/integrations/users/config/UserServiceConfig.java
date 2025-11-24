package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.integrations.users.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class UserServiceConfig {

    @Bean(name = "userServiceClient")
    public RestClient userServiceClient(@Value("${user-service.base-uri}") String baseUri) {
        return RestClient.builder()
                .baseUrl(baseUri)
                .build();
    }


}
