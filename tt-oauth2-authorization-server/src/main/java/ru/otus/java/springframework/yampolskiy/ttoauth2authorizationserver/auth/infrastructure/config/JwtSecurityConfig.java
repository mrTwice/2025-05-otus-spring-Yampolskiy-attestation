package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.infrastructure.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.core.service.JwkService;

import java.util.List;

@Configuration
public class JwtSecurityConfig {
    @Bean
    public JWKSource<SecurityContext> jwkSource(JwkService jwkService) {
        return (jwkSelector, ctx)
                -> jwkSelector.select(jwkService.loadActiveJwkSet());
    }

    @Bean
    public JwtEncoder jwtEncoder(JwkService jwkService) {
        JWKSource<SecurityContext> jwkSource = (jwkSelector, context) ->
                jwkSelector.select(jwkService.loadSigningJwkSet());

        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder(JwkService jwkService) {
        JWKSource<SecurityContext> jwkSource = (jwkSelector, ctx) ->
                jwkSelector.select(jwkService.loadValidationJwkSet());

        JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, jwkSource);

        DefaultJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        jwtProcessor.setJWSKeySelector(keySelector);

        return new NimbusJwtDecoder(jwtProcessor);
    }

    @Bean
    public OAuth2TokenGenerator<?> tokenGenerator(
            JwtEncoder jwtEncoder,
            OAuth2TokenCustomizer<JwtEncodingContext> combinedCustomizer) {
        JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
        jwtGenerator.setJwtCustomizer(combinedCustomizer);
        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(
                jwtGenerator,
                accessTokenGenerator,
                refreshTokenGenerator);
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> combinedCustomizer(
            List<OAuth2TokenCustomizer<JwtEncodingContext>> customizers
    ) {
        return context -> {
            for (OAuth2TokenCustomizer<JwtEncodingContext> customizer : customizers) {
                customizer.customize(context);
            }
        };
    }

}
