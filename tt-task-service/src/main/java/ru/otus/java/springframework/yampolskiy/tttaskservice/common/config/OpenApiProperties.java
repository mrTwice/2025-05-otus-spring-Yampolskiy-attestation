package ru.otus.java.springframework.yampolskiy.tttaskservice.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "openapi")
public class OpenApiProperties {
    private String serverUrl;

    private String authUrl;
}