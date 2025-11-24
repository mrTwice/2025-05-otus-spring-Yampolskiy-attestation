package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.oidc.infrastructrue.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationToken;

@Slf4j
public class OidcUserInfoLoggingAuthenticationProvider implements AuthenticationProvider {

    private final OidcUserInfoAuthenticationProvider delegate;

    public OidcUserInfoLoggingAuthenticationProvider(OidcUserInfoAuthenticationProvider delegate) {
        log.info("➡️ Инициализируем OidcUserInfoLoggingAuthenticationProvider");
        this.delegate = delegate;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("🧠 /userinfo auth: {} | details: {}", authentication.getClass().getSimpleName(), authentication);
        try {
            Authentication result = delegate.authenticate(authentication);
            log.info("✅ /userinfo auth success: {}", result);
            return result;
        } catch (AuthenticationException ex) {
            log.warn("❌ /userinfo auth failed: {} — {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OidcUserInfoAuthenticationToken.class.isAssignableFrom(authentication);
    }
}