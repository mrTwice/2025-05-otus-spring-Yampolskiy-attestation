package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.core.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.core.mapper.AuthorizationMapper;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.core.repository.AuthorizationRepository;

import java.util.Optional;

@Service
public class DatabaseAuthorizationService implements OAuth2AuthorizationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseAuthorizationService.class);

    private final AuthorizationRepository authorizationRepository;

    private final AuthorizationMapper authorizationMapper;

    public DatabaseAuthorizationService(
            AuthorizationRepository authorizationRepository,
            AuthorizationMapper authorizationMapper
    ) {
        this.authorizationRepository = authorizationRepository;
        this.authorizationMapper = authorizationMapper;
        LOGGER.info("🔥 JpaOAuth2AuthorizationService initialized");
    }

    @Override
    @Transactional
    public void save(OAuth2Authorization authorization) {
        OAuth2AccessToken accessToken = getToken(authorization, OAuth2AccessToken.class);
        OAuth2RefreshToken refreshToken = getToken(authorization, OAuth2RefreshToken.class);
        OidcIdToken idToken = getToken(authorization, OidcIdToken.class);

        String state = authorization.getAttribute(OAuth2ParameterNames.STATE);

        logTokens(accessToken, refreshToken, idToken, state);
        logTokenMetadata(authorization, OAuth2AccessToken.class, "Access Token");
        logTokenMetadata(authorization, OAuth2RefreshToken.class, "Refresh Token");

        if (refreshToken == null) {
            LOGGER.warn("❌ Refresh Token НЕ СОЗДАН! Проверь конфигурацию клиента и scope!");
        }

        authorizationRepository.save(authorizationMapper.from(authorization));
    }

    @Override
    @Transactional
    public void remove(OAuth2Authorization authorization) {
        LOGGER.info("Удаляем токен: {}", authorization.getId());
        authorizationRepository.deleteById(authorization.getId());
        if (!authorizationRepository.existsById(authorization.getId())) {
            LOGGER.warn("Токен уже удалён или не найден: {}", authorization.getId());
        } else {
            LOGGER.warn("Токен не удалён: {}", authorization.getId());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public OAuth2Authorization findById(String id) {
        return authorizationRepository.findById(id)
                .map(authorizationMapper::toAuthorization)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        LOGGER.info("🔥 [findByToken] token = {}, tokenType = {}", token, tokenType);

        return getOAuth2Authorization(token, tokenType);
    }

    private <T extends OAuth2Token> T getToken(OAuth2Authorization authorization,
                                               Class<T> tokenClass) {
        return Optional.ofNullable(authorization.getToken(tokenClass))
                .map(OAuth2Authorization.Token::getToken)
                .orElse(null);
    }

    private void logTokens(OAuth2AccessToken accessToken,
                           OAuth2RefreshToken refreshToken,
                           OidcIdToken idToken,
                           String state) {

        LOGGER.info("🧪 Сохраняем токены: AccessToken: {}, RefreshToken: {}, IdToken: {}, State: {}",
                accessToken != null ? "✅" : "❌",
                refreshToken != null ? "✅" : "❌",
                idToken != null ? "✅" : "❌",
                state != null ? state : "❌ (не передан)");
    }

    private void logTokenMetadata(OAuth2Authorization authorization,
                                  Class<? extends OAuth2Token> tokenClass,
                                  String tokenName) {

        OAuth2Authorization.Token<?> token = authorization.getToken(tokenClass);

        Object metadata = (token != null && token.getMetadata() != null)
                ? token.getMetadata()
                : "null";

        LOGGER.info("🔍 {} Metadata: {}", tokenName, metadata);
    }

    private OAuth2Authorization getOAuth2Authorization(String token, OAuth2TokenType tokenType) {
        OAuth2Authorization authorization = null;

        if (tokenType == null) {
            authorization = authorizationRepository.findByTokenValue(token)
                    .map(authorizationMapper::toAuthorization)
                    .orElse(null);
        } else {
            authorization = findAuthorizationByTokenAndType(token, tokenType);
        }

        if (authorization == null) {
            LOGGER.warn("❌ Токен не найден: {}, тип: {}", token, tokenType != null ? tokenType.getValue() : "null");
        } else {
            LOGGER.info("✅ Найдена авторизация: {}", authorization.getId());
        }
        return authorization;
    }

    private OAuth2Authorization findAuthorizationByTokenAndType(String token, OAuth2TokenType tokenType) {

        return switch (tokenType.getValue()) {
            case "access_token" -> authorizationRepository.findByAccessTokenValue(token)
                    .map(authorizationMapper::toAuthorization)
                    .orElse(null);

            case "refresh_token" -> authorizationRepository.findByRefreshTokenValue(token)
                    .map(authorizationMapper::toAuthorization)
                    .orElse(null);

            case "authorization_code", "code" -> authorizationRepository.findByAuthorizationCodeValue(token)
                    .map(authorizationMapper::toAuthorization)
                    .orElse(null);

            case "state" -> authorizationRepository.findByState(token)
                    .map(authorizationMapper::toAuthorization)
                    .orElse(null);

            default -> throw new IllegalArgumentException("Unsupported token type: " + tokenType.getValue());
        };
    }
}

