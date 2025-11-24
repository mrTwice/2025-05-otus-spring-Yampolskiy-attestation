package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.api.dto.TokenIntrospectionResponse;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.core.entity.Oauth2Authorization;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.common.exception.EntityNotFoundException;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.core.repository.AuthorizationRepository;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final AuthorizationRepository authorizationRepository;

    @Transactional
    public Oauth2Authorization saveAuthorization(Oauth2Authorization oauth2Authorization) {
        return authorizationRepository.save(oauth2Authorization);
    }

    @Transactional(readOnly = true)
    public Oauth2Authorization getAuthorizationById(String id) {
        return authorizationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Authorization not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Oauth2Authorization getAuthorizationByAccessToken(String accessTokenValue) {
        return authorizationRepository.findByAccessTokenValue(accessTokenValue)
                .orElseThrow(() -> new EntityNotFoundException("Authorization not found for access token"));
    }

    @Transactional(readOnly = true)
    public Oauth2Authorization getAuthorizationByRefreshToken(String refreshTokenValue) {
        return authorizationRepository.findByRefreshTokenValue(refreshTokenValue)
                .orElseThrow(() -> new EntityNotFoundException("Authorization not found for refresh token"));
    }

    @Transactional
    public void deleteAuthorizationByAccessToken(String accessTokenValue) {
        Oauth2Authorization oauth2Authorization = getAuthorizationByAccessToken(accessTokenValue);
        authorizationRepository.delete(oauth2Authorization);
    }

    @Transactional
    public void deleteAuthorizationByRefreshToken(String refreshTokenValue) {
        Oauth2Authorization oauth2Authorization = getAuthorizationByRefreshToken(refreshTokenValue);
        authorizationRepository.delete(oauth2Authorization);
    }

    @Transactional(readOnly = true)
    public List<Oauth2Authorization> getAuthorizationsByClientId(String registeredClientId) {
        return authorizationRepository.findByRegisteredClientId(registeredClientId);
    }

    @Transactional
    public void revokeAllTokensForUser(String username) {
        List<Oauth2Authorization> oauth2Authorizations = authorizationRepository.findByPrincipalName(username);
        authorizationRepository.deleteAll(oauth2Authorizations);
    }

    @Transactional(readOnly = true)
    public TokenIntrospectionResponse validateAccessToken(String accessTokenValue) {
        Oauth2Authorization oauth2Authorization = authorizationRepository.findByAccessTokenValue(accessTokenValue)
                .orElse(null);

        if (oauth2Authorization == null || oauth2Authorization.getAccessTokenExpiresAt() == null ||
                oauth2Authorization.getAccessTokenExpiresAt().isBefore(Instant.now())) {
            return TokenIntrospectionResponse.builder()
                    .active(false)
                    .build();
        }

        return TokenIntrospectionResponse.builder()
                .active(true)
                .clientId(oauth2Authorization.getRegisteredClientId())
                .scopes(Set.of(oauth2Authorization.getAccessTokenScopes().split(",")))
                .expiresAt(oauth2Authorization.getAccessTokenExpiresAt())
                .build();
    }
}