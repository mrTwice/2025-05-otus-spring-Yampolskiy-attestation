package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.client.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeAuthenticationProvider;

@Slf4j
public class LoggingAuthorizationCodeAuthenticationProvider implements AuthenticationProvider {

    private final OAuth2AuthorizationCodeAuthenticationProvider delegate;

    public LoggingAuthorizationCodeAuthenticationProvider(OAuth2AuthorizationCodeAuthenticationProvider delegate) {
        log.info("➡️ Инициализируем LoggingAuthorizationCodeAuthenticationProvider");
        this.delegate = delegate;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("🔄 Authorization Code flow: {}", authentication.getName());
        return delegate.authenticate(authentication);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return delegate.supports(authentication);
    }
}