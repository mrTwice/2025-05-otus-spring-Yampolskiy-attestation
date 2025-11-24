package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;

@Configuration
public class AuthorizationServerSettingsConfig {
    @Bean
    public AuthorizationServerSettings authorizationServerSettings(
            @Value("${auth-server.base-uri}") String issuer
    ) {
        return AuthorizationServerSettings.builder()
                .issuer(issuer)
                .tokenEndpoint("/oauth2/token")
                .authorizationEndpoint("/oauth2/authorize")
                .tokenIntrospectionEndpoint("/oauth2/introspect")
                .tokenRevocationEndpoint("/oauth2/revoke")
                .oidcUserInfoEndpoint("/oauth2/userinfo") //TODO: переделать на стандарт
                .oidcLogoutEndpoint("/logout")
                .jwkSetEndpoint("/oauth2/jwks")
                .build();
    }

    @Bean
    public TokenSettings tokenSettings() {
        return TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .accessTokenTimeToLive(Duration.ofHours(1))
                .refreshTokenTimeToLive(Duration.ofDays(30))
                .reuseRefreshTokens(true)
                .idTokenSignatureAlgorithm(SignatureAlgorithm.RS256)
                .build();
    }
}
