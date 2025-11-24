package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.integrations.users.dto.UserPrincipalDTO;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.oidc.service.UserAuthCache;

import java.util.ArrayList;

@Configuration
@Slf4j
public class AccessTokenCustomizerConfig {

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> accessTokenCustomizer(
            UserAuthCache userAuthCache
    ) {
        log.info("🛠 AccessTokenCustomizer initializing");
        return context -> customizeAccessToken(context, userAuthCache);
    }

    private void customizeAccessToken(JwtEncodingContext context, UserAuthCache userAuthCache) {
        if (!OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
            return;
        }

        String username = context.getPrincipal().getName();
        log.info("🛠 ACCESS_TOKEN for: {}", username);

        UserPrincipalDTO user = userAuthCache.get(username);
        if (user == null) {
            log.warn("⚠️ UserPrincipalDTO not found in UserAuthCache for: {}", username);
            return;
        }

        log.info("✅ Found user in UserAuthCache: {}", user.getUsername());

        applyClaims(context, user);
        userAuthCache.remove(username);
    }

    private void applyClaims(JwtEncodingContext context, UserPrincipalDTO user) {
        context.getClaims().claims(claims -> {
            claims.put("permissions", new ArrayList<>(user.getPermissions()));
            claims.put("roles", new ArrayList<>(user.getRoles()));
            claims.put("scope", String.join(" ", user.getPermissions()));
            claims.put("sub", user.getOidcSubject().toString());
            claims.put("user_name", user.getUsername());
            claims.put("client_id", context.getRegisteredClient().getClientId());
        });
    }
}
