package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.infrastructure.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class HttpRequestLoggingInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestLoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            LOGGER.warn(
                    "➡️ Incoming GET request: [{}] {}?{}",
                    request.getMethod(),
                    request.getRequestURI(),
                    request.getQueryString());
        }
        return true;
    }
}