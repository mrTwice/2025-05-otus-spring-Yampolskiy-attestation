package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.infrastructure.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;

public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        logRequest(request);
        logAuth(request);

        try {
            filterChain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void logRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String params = request.getParameterMap().entrySet().stream()
                .map(e -> e.getKey() + "=" + String.join(",", e.getValue()))
                .collect(Collectors.joining(", "));

        LOGGER.info("📥 [LOGGING FILTER] Запрос: {}", uri);
        LOGGER.info("➡️ Метод: {}, Параметры: {}", method, params);
    }

    private void logAuth(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            LOGGER.info("🔑 Authorization: {}", authHeader);
        } else {
            LOGGER.warn("⚠️ Authorization header отсутствует");
        }
    }
}