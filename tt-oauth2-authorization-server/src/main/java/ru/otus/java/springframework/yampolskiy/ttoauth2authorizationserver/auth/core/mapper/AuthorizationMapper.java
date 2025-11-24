package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.core.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Service;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.core.entity.Oauth2Authorization;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.client.service.SecurityRegisteredClientRepository;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.util.AuthorizationRequestSerializer;

import java.io.IOException;
import java.security.Principal;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthorizationMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationMapper.class);

    private static final String ATTR_AUTHZ_REQ_SERIALIZED = "authorization_request_serialized";

    private static final String ATTR_PRINCIPAL_NAME = "principal_name";

    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {};

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private final SecurityRegisteredClientRepository registeredClientRepository;



    public Oauth2Authorization from(OAuth2Authorization authorization) {
        Oauth2Authorization entity = buildBaseEntity(authorization);

        Map<String, Object> attributes = buildAttributesForPersist(authorization);
        entity.setAttributes(writeJson(attributes, "Ошибка сериализации attributes"));

        mapAuthorizationCode(authorization, entity);
        mapAccessToken(authorization, entity);
        mapRefreshToken(authorization, entity);
        mapIdToken(authorization, entity);

        return entity;
    }

    public OAuth2Authorization toAuthorization(Oauth2Authorization entity) {
        RegisteredClient client = getRegisteredClientOrThrow(entity.getRegisteredClientId());

        OAuth2Authorization.Builder builder = initAuthorizationBuilder(entity, client);
        restoreAttributes(entity, builder);

        restoreAuthorizationCode(entity, builder);
        restoreAccessToken(entity, builder);
        restoreRefreshToken(entity, builder);
        restoreIdToken(entity, builder);

        return builder.build();
    }

    private Oauth2Authorization buildBaseEntity(OAuth2Authorization authorization) {
        return Oauth2Authorization.builder()
                .id(authorization.getId())
                .registeredClientId(authorization.getRegisteredClientId())
                .principalName(authorization.getPrincipalName())
                .authorizationGrantType(authorization.getAuthorizationGrantType().getValue())
                .authorizedScopes(String.join(",", authorization.getAuthorizedScopes()))
                .state(authorization.getAttribute("state"))
                .build();
    }

    private Map<String, Object> buildAttributesForPersist(OAuth2Authorization authorization) {
        Map<String, Object> attributes = new HashMap<>(authorization.getAttributes());

        attributes.entrySet().removeIf(entry ->
                entry.getValue() instanceof Authentication ||
                        entry.getValue() instanceof Principal
        );
        attributes.remove(Principal.class.getName());
        attributes.remove(Authentication.class.getName());

        OAuth2AuthorizationRequest authzRequest =
                authorization.getAttribute(OAuth2AuthorizationRequest.class.getName());

        if (authzRequest != null) {
            String serialized = AuthorizationRequestSerializer.serialize(authzRequest);
            attributes.put(ATTR_AUTHZ_REQ_SERIALIZED, serialized);
        }

        attributes.put(ATTR_PRINCIPAL_NAME, authorization.getPrincipalName());

        return attributes;
    }

    private void mapAuthorizationCode(OAuth2Authorization source, Oauth2Authorization target) {
        var code = source.getToken(OAuth2AuthorizationCode.class);
        if (code == null) {
            return;
        }

        var token = code.getToken();
        target.setAuthorizationCodeValue(token.getTokenValue());
        target.setAuthorizationCodeIssuedAt(token.getIssuedAt());
        target.setAuthorizationCodeExpiresAt(token.getExpiresAt());
    }

    private void mapAccessToken(OAuth2Authorization source, Oauth2Authorization target) {
        var access = source.getToken(OAuth2AccessToken.class);
        if (access == null) {
            return;
        }

        var token = access.getToken();
        target.setAccessTokenValue(token.getTokenValue());
        target.setAccessTokenIssuedAt(token.getIssuedAt());
        target.setAccessTokenExpiresAt(token.getExpiresAt());
        target.setAccessTokenType(token.getTokenType().getValue());
        target.setAccessTokenScopes(String.join(",", token.getScopes()));

        Map<String, Object> metadata = new HashMap<>(access.getMetadata());
        convertMetadataDates(metadata);
        target.setAccessTokenMetadata(
                writeJson(metadata, "Ошибка сериализации access_token_metadata")
        );
    }

    private void mapRefreshToken(OAuth2Authorization source, Oauth2Authorization target) {
        var refresh = source.getToken(OAuth2RefreshToken.class);
        if (refresh == null) {
            LOGGER.warn("❌ [DEBUG] Refresh Token НЕ создан!");
            return;
        }

        var token = refresh.getToken();
        target.setRefreshTokenValue(token.getTokenValue());
        target.setRefreshTokenIssuedAt(token.getIssuedAt());
        target.setRefreshTokenExpiresAt(token.getExpiresAt());

        Map<String, Object> metadata = new HashMap<>(refresh.getMetadata());
        convertMetadataDates(metadata);
        target.setRefreshTokenMetadata(
                writeJson(metadata, "Ошибка сериализации refresh_token_metadata")
        );
    }

    private void mapIdToken(OAuth2Authorization source, Oauth2Authorization target) {
        var idToken = source.getToken(OidcIdToken.class);
        if (idToken == null) {
            return;
        }

        var token = idToken.getToken();
        target.setIdTokenValue(token.getTokenValue());
        target.setIdTokenIssuedAt(token.getIssuedAt());
        target.setIdTokenExpiresAt(token.getExpiresAt());

        Map<String, Object> claims = new HashMap<>(token.getClaims());
        convertMetadataDates(claims);
        target.setIdTokenMetadata(
                writeJson(claims, "Ошибка сериализации ID Token claims")
        );
    }

    private RegisteredClient getRegisteredClientOrThrow(String clientId) {
        RegisteredClient client = registeredClientRepository.findById(clientId);
        if (client == null) {
            throw new IllegalArgumentException("Client not found: " + clientId);
        }
        return client;
    }

    private OAuth2Authorization.Builder initAuthorizationBuilder(
            Oauth2Authorization entity,
            RegisteredClient client
    ) {
        OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(client)
                .id(entity.getId())
                .principalName(entity.getPrincipalName())
                .authorizationGrantType(new AuthorizationGrantType(entity.getAuthorizationGrantType()))
                .authorizedScopes(Set.of(entity.getAuthorizedScopes().split(",")));

        if (entity.getState() != null) {
            builder.attribute("state", entity.getState());
        }

        return builder;
    }

    private void restoreAttributes(Oauth2Authorization entity, OAuth2Authorization.Builder builder) {
        Map<String, Object> attributes =
                readJsonToMap(entity.getAttributes(), "Ошибка десериализации attributes");

        restoreAuthorizationRequest(attributes);
        restoreAuthentication(attributes);

        builder.attributes(attrs -> attrs.putAll(attributes));
    }

    private void restoreAuthorizationRequest(Map<String, Object> attributes) {
        if (!attributes.containsKey(ATTR_AUTHZ_REQ_SERIALIZED)) {
            return;
        }

        String serialized = (String) attributes.get(ATTR_AUTHZ_REQ_SERIALIZED);
        OAuth2AuthorizationRequest deserialized =
                AuthorizationRequestSerializer.deserialize(serialized);

        attributes.put(OAuth2AuthorizationRequest.class.getName(), deserialized);
    }

    private void restoreAuthentication(Map<String, Object> attributes) {
        if (!attributes.containsKey(ATTR_PRINCIPAL_NAME)) {
            return;
        }

        String username = (String) attributes.get(ATTR_PRINCIPAL_NAME);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(username, null, List.of());

        attributes.put(Principal.class.getName(), authentication);
    }

    private void restoreAuthorizationCode(Oauth2Authorization entity,
                                          OAuth2Authorization.Builder builder) {
        if (entity.getAuthorizationCodeValue() == null) {
            return;
        }

        builder.token(new OAuth2AuthorizationCode(
                entity.getAuthorizationCodeValue(),
                entity.getAuthorizationCodeIssuedAt(),
                entity.getAuthorizationCodeExpiresAt()
        ));
    }

    private void restoreAccessToken(Oauth2Authorization entity,
                                    OAuth2Authorization.Builder builder) {
        if (entity.getAccessTokenValue() == null) {
            return;
        }

        Map<String, Object> metadata =
                readJsonToMap(entity.getAccessTokenMetadata(),
                        "Ошибка десериализации access_token_metadata");

        convertMetadataDates(metadata);
        LOGGER.info("🔄 Загружены метаданные Access Token: {}", metadata);

        builder.token(
                new OAuth2AccessToken(
                        OAuth2AccessToken.TokenType.BEARER,
                        entity.getAccessTokenValue(),
                        entity.getAccessTokenIssuedAt(),
                        entity.getAccessTokenExpiresAt(),
                        Set.of(entity.getAccessTokenScopes().split(","))
                ),
                claims -> claims.putAll(metadata)
        );
    }

    private void restoreRefreshToken(Oauth2Authorization entity,
                                     OAuth2Authorization.Builder builder) {
        if (entity.getRefreshTokenValue() == null) {
            return;
        }

        Map<String, Object> metadata =
                readJsonToMap(entity.getRefreshTokenMetadata(),
                        "Ошибка десериализации refresh_token_metadata");

        convertMetadataDates(metadata);
        LOGGER.info("🔄 Загружены метаданные Refresh Token: {}", metadata);

        builder.token(
                new OAuth2RefreshToken(
                        entity.getRefreshTokenValue(),
                        entity.getRefreshTokenIssuedAt(),
                        entity.getRefreshTokenExpiresAt()
                ),
                claims -> claims.putAll(metadata)
        );
    }

    private void restoreIdToken(Oauth2Authorization entity,
                                OAuth2Authorization.Builder builder) {
        if (entity.getIdTokenValue() == null) {
            return;
        }

        Map<String, Object> claims =
                readJsonToMap(entity.getIdTokenMetadata(),
                        "Ошибка десериализации ID Token claims");

        convertMetadataDates(claims);

        builder.token(new OidcIdToken(
                entity.getIdTokenValue(),
                entity.getIdTokenIssuedAt(),
                entity.getIdTokenExpiresAt(),
                claims
        ));
    }

    private String writeJson(Map<String, Object> data, String errorMessage) {
        try {
            return OBJECT_MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(errorMessage, e);
        }
    }

    private Map<String, Object> readJsonToMap(String json, String errorMessage) {
        try {
            return OBJECT_MAPPER.readValue(json, MAP_TYPE);
        } catch (IOException e) {
            throw new IllegalStateException(errorMessage, e);
        }
    }

    private void convertMetadataDates(Map<String, Object> metadata) {
        if (metadata.containsKey("metadata.token.claims")) {
            Object claimsObj = metadata.get("metadata.token.claims");
            if (claimsObj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> claims = (Map<String, Object>) claimsObj;
                convertInstantField(claims, "iat");
                convertInstantField(claims, "exp");
                convertInstantField(claims, "nbf");
            }
        }
    }

    private void convertInstantField(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof String) {
            try {
                map.put(key, Instant.parse((String) value));
            } catch (DateTimeParseException e) {
                LOGGER.error("Ошибка преобразования даты {}: {}", key, value, e);
            }
        }
    }

}

