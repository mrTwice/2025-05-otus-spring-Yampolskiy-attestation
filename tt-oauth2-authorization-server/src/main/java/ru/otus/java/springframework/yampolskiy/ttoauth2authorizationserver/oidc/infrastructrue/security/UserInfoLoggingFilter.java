package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.oidc.infrastructrue.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserInfoLoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getRequestURI().equals("/oauth2/userinfo")) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            LOGGER.info("👁 SecurityContext authentication на /userinfo: {}", auth);
        }

        filterChain.doFilter(request, response);
    }
}
