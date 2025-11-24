package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.client.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "swagger.user-service")
public class SwaggerUserServiceProperties {
    private String redirectUrl;

}
