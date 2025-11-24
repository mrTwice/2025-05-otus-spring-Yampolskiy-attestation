package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.oidc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationProvider;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.oidc.mapper.OidcUserInfoClaimsMapper;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.oidc.infrastructrue.security.OidcUserInfoLoggingAuthenticationProvider;

@Configuration
public class OidcUserInfoAuthenticationConfig {

    @Bean
    public AuthenticationProvider loggingOidcUserInfoAuthenticationProvider(
            OAuth2AuthorizationService authorizationService,
            OidcUserInfoClaimsMapper userInfoMapper
    ) {
        OidcUserInfoAuthenticationProvider baseProvider = new OidcUserInfoAuthenticationProvider(authorizationService);
        userInfoMapper.configureUserInfoMapper(baseProvider);
        return new OidcUserInfoLoggingAuthenticationProvider(baseProvider);
    }
}
