package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.infrastructure.logging.HttpRequestLoggingInterceptor;

@Configuration
public class WebMvcLoggingConfig implements WebMvcConfigurer {
    private final HttpRequestLoggingInterceptor httpRequestLoggingInterceptor;

    public WebMvcLoggingConfig(HttpRequestLoggingInterceptor httpRequestLoggingInterceptor) {
        this.httpRequestLoggingInterceptor = httpRequestLoggingInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(httpRequestLoggingInterceptor);
    }
}