package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.infrastructure.logging;

import jakarta.servlet.Filter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@Order(1)
public class HeaderLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;

        log.info("Host: {}", request.getHeader("Host"));
        log.info("X-Forwarded-Host: {}", request.getHeader("X-Forwarded-Host"));
        log.info("X-Forwarded-Proto: {}", request.getHeader("X-Forwarded-Proto"));
        log.info("X-Forwarded-Port: {}", request.getHeader("X-Forwarded-Port"));
        log.info("Forwarded: {}", request.getHeader("Forwarded"));

        chain.doFilter(req, res);
    }
}