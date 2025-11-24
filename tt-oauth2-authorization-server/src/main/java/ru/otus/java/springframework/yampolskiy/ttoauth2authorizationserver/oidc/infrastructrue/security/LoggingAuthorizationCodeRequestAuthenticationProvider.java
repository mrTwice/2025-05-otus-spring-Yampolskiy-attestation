package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.oidc.infrastructrue.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationProvider;

@Slf4j
public class LoggingAuthorizationCodeRequestAuthenticationProvider implements AuthenticationProvider {

    private final OAuth2AuthorizationCodeRequestAuthenticationProvider delegate;

    public LoggingAuthorizationCodeRequestAuthenticationProvider(
            OAuth2AuthorizationCodeRequestAuthenticationProvider delegate
    ) {
        log.info("➡️ Инициализируем LoggingAuthorizationCodeRequestAuthenticationProvider");
        this.delegate = delegate;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("📥 Обработка /authorize запроса: {}", authentication.getClass().getSimpleName());
        try {
            Authentication result = delegate.authenticate(authentication);
            log.info("✅ Успешная авторизация code request: {}", result);
            return result;
        } catch (AuthenticationException e) {
            log.warn("❌ Ошибка авторизации code request: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return delegate.supports(authentication);
    }
}
