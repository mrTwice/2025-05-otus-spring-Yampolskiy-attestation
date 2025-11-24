package ru.otus.java.springframework.yampolskiy.tttaskservice.common.securiry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.List;
import java.util.UUID;
import java.util.Arrays;
import java.util.Objects;
import java.util.Collection;

@Slf4j
public abstract class AbstractAccessPolicy {

    protected boolean hasPermission(Authentication authentication, String permission) {
        Jwt jwt = extractJwt(authentication);
        if (jwt == null) {
            return false;
        }

        List<String> permissions = getClaimAsList(jwt, "permissions");
        List<String> scopes = jwt.getClaimAsStringList("scope");

        log.debug("🔍 Checking permission [{}], permissions: {}, scopes: {}", permission, permissions, scopes);

        return permissions.contains(permission) || (scopes != null && scopes.contains(permission));
    }

    protected boolean hasAnyPermission(Authentication authentication, String... permissionsToCheck) {
        Jwt jwt = extractJwt(authentication);
        if (jwt == null) {
            return false;
        }

        List<String> permissions = getClaimAsList(jwt, "permissions");
        List<String> scopes = jwt.getClaimAsStringList("scope");

        return Arrays.stream(permissionsToCheck)
                .anyMatch(p -> permissions.contains(p) || (scopes != null && scopes.contains(p)));
    }

    protected Jwt extractJwt(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt jwt) {
            return jwt;
        }

        return null;
    }

    protected List<String> getClaimAsList(Jwt jwt, String claimName) {
        Object claim = jwt.getClaims().get(claimName);
        if (claim instanceof Collection<?> collection) {
            return collection.stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .toList();
        }
        return List.of();
    }

    protected UUID getUserId(Authentication authentication) {
        Jwt jwt = extractJwt(authentication);
        if (jwt == null) {
            return null;
        }
        try {
            return UUID.fromString(jwt.getSubject());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    protected boolean isSelf(Authentication authentication, UUID userId) {
        UUID currentUserId = getUserId(authentication);
        return currentUserId != null && currentUserId.equals(userId);
    }
}
