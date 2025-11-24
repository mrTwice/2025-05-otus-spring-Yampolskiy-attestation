package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/oauth2/**")
                        .allowedOrigins("http://localhost:9591")
                        .allowedMethods("*")
                        .allowedHeaders("*");
            }
        };
    }
}
