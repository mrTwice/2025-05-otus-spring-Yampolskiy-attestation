package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.infrastructure.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

@Component
public class TokenEndpointLoggingFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenEndpointLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getRequestURI().equals("/oauth2/token")) {
            LOGGER.info(
                    "📥 Запрос на /oauth2/token: method={}, params={}",
                    request.getMethod(),
                    request.getParameterMap());

            Enumeration<String> headers = request.getHeaderNames();
            while (headers.hasMoreElements()) {
                String header = headers.nextElement();
                LOGGER.info("🔹 Header: {} = {}", header, request.getHeader(header));
            }
        }

        filterChain.doFilter(request, response);
    }
}
