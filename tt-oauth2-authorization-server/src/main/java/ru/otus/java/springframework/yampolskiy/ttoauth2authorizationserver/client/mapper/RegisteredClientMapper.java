package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.client.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.client.entity.RegisteredClientEntity;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class RegisteredClientMapper {

    private static final Logger LOGGER = Logger.getLogger(RegisteredClientMapper.class.getName());

    public RegisteredClientEntity from(RegisteredClient client) {
        return RegisteredClientEntity.builder()
                .id(client.getId())
                .clientId(client.getClientId())
                .clientIdIssuedAt(Optional.ofNullable(client.getClientIdIssuedAt()).orElse(Instant.now()))
                .clientSecret(client.getClientSecret())
                .clientSecretExpiresAt(client.getClientSecretExpiresAt())
                .clientName(client.getClientName())
                .clientAuthenticationMethods(
                        client.getClientAuthenticationMethods().stream()
                                .map(ClientAuthenticationMethod::getValue)
                                .toList()
                )
                .authorizationGrantTypes(
                        client.getAuthorizationGrantTypes().stream()
                                .map(AuthorizationGrantType::getValue)
                                .toList()
                )
                .redirectUris(new ArrayList<>(client.getRedirectUris()))
                .scopes(new ArrayList<>(client.getScopes()))
                .clientSettings(client.getClientSettings().getSettings())
                .tokenSettings(client.getTokenSettings().getSettings())
                .build();
    }

    public RegisteredClient toRegisteredClient(RegisteredClientEntity entity) {
        LOGGER.info("MAPPING CLIENT: " + entity.getClientId());
        LOGGER.info(" → Auth methods: " + entity.getClientAuthenticationMethods());

        Map<String, Object> tokenSettingsMap = entity.getTokenSettings();
        Map<String, Object> clientSettingsMap = entity.getClientSettings();

        TokenSettings.Builder tokenSettingsBuilder = TokenSettings.builder();

        Object accessTokenFormatRaw = tokenSettingsMap.get("settings.token.access-token-format");
        if (accessTokenFormatRaw instanceof Map<?, ?> formatMap) {
            String value = (String) formatMap.get("value");
            if ("self-contained".equalsIgnoreCase(value)) {
                tokenSettingsBuilder.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED);
            } else if ("reference".equalsIgnoreCase(value)) {
                tokenSettingsBuilder.accessTokenFormat(OAuth2TokenFormat.REFERENCE);
            }
        }

        tokenSettingsMap.forEach(setupToken(tokenSettingsBuilder));
        ClientSettings clientSettings = ClientSettings.withSettings(clientSettingsMap).build();
        TokenSettings tokenSettings = tokenSettingsBuilder.build();

        return getRegisteredClient(entity, clientSettings, tokenSettings);
    }

    private static BiConsumer<String, Object> setupToken(TokenSettings.Builder tokenSettingsBuilder) {
        return (key, val) -> {
            if ("settings.token.access-token-time-to-live".equals(key) && val instanceof Number number) {
                tokenSettingsBuilder.accessTokenTimeToLive(Duration.ofSeconds(number.longValue()));
            } else if ("settings.token.refresh-token-time-to-live".equals(key) && val instanceof Number number) {
                tokenSettingsBuilder.refreshTokenTimeToLive(Duration.ofSeconds(number.longValue()));
            } else if ("settings.token.authorization-code-time-to-live".equals(key) && val instanceof Number number) {
                tokenSettingsBuilder.authorizationCodeTimeToLive(Duration.ofSeconds(number.longValue()));
            } else if ("settings.token.device-code-time-to-live".equals(key) && val instanceof Number number) {
                tokenSettingsBuilder.deviceCodeTimeToLive(Duration.ofSeconds(number.longValue()));
            } else if ("settings.token.id-token-signature-algorithm".equals(key) && val instanceof String alg) {
                try {
                    tokenSettingsBuilder.idTokenSignatureAlgorithm(SignatureAlgorithm.from(alg));
                } catch (Exception e) {
                    LOGGER.warning("⚠️ Неизвестный алгоритм подписи ID Token: " + alg);
                }
            } else if (!"settings.token.access-token-format".equals(key)) {
                tokenSettingsBuilder.setting(key, val);
            }
        };
    }

    private static RegisteredClient getRegisteredClient(
            RegisteredClientEntity entity,
            ClientSettings clientSettings,
            TokenSettings tokenSettings
    ) {
        return RegisteredClient.withId(entity.getId())
                .clientId(entity.getClientId())
                .clientSecret(entity.getClientSecret())
                .clientIdIssuedAt(entity.getClientIdIssuedAt())
                .clientSecretExpiresAt(entity.getClientSecretExpiresAt())
                .clientName(entity.getClientName())
                .clientAuthenticationMethods(methods ->
                        Optional.ofNullable(entity.getClientAuthenticationMethods())
                                .orElse(List.of())
                                .forEach(m -> methods.add(new ClientAuthenticationMethod(m)))
                )
                .authorizationGrantTypes(grantTypes ->
                        Optional.ofNullable(entity.getAuthorizationGrantTypes())
                                .orElse(List.of())
                                .forEach(g -> grantTypes.add(new AuthorizationGrantType(g)))
                )
                .redirectUris(uris -> uris.addAll(Optional.ofNullable(entity.getRedirectUris()).orElse(List.of())))
                .scopes(scopes -> scopes.addAll(Optional.ofNullable(entity.getScopes()).orElse(List.of())))
                .clientSettings(clientSettings)
                .tokenSettings(tokenSettings)
                .build();
    }
}
