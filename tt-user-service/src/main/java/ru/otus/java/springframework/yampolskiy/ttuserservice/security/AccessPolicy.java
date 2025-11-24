package ru.otus.java.springframework.yampolskiy.ttuserservice.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.Arrays;
import java.util.Objects;
import java.util.Collection;

@Slf4j
@Component("accessPolicy")
public class AccessPolicy {

    public boolean hasPermission(Authentication authentication, String permission) {
        Jwt jwt = extractJwt(authentication);
        if (jwt == null) {
            return false;
        }

        List<String> permissions = getClaimAsList(jwt, "permissions");
        List<String> scopes = jwt.getClaimAsStringList("scope");

        log.info("🔍 Permissions: {}", permissions);
        log.info("🔍 Scopes: {}", scopes);

        return permissions.contains(permission) || (scopes != null && scopes.contains(permission));
    }

    public boolean hasAnyPermission(Authentication authentication, String... permissionsToCheck) {
        Jwt jwt = extractJwt(authentication);
        if (jwt == null) {
            return false;
        }

        List<String> permissions = getClaimAsList(jwt, "permissions");
        List<String> scopes = jwt.getClaimAsStringList("scope");

        return Arrays.stream(permissionsToCheck)
                .anyMatch(p -> permissions.contains(p) || (scopes != null && scopes.contains(p)));
    }

    public boolean hasRole(Authentication authentication, String role) {
        Jwt jwt = extractJwt(authentication);
        if (jwt == null) {
            return false;
        }

        List<String> roles = getClaimAsList(jwt, "roles");
        return roles.contains(role);
    }

    public boolean hasAnyRole(Authentication authentication, String... rolesToCheck) {
        Jwt jwt = extractJwt(authentication);
        if (jwt == null) {
            return false;
        }

        List<String> roles = getClaimAsList(jwt, "roles");
        return Arrays.stream(rolesToCheck).anyMatch(roles::contains);
    }

    public boolean hasPermissionOrRole(Authentication authentication, String permission, String role) {
        return hasPermission(authentication, permission) || hasRole(authentication, role);
    }

    public boolean isSelf(Authentication authentication, UUID userId) {
        Jwt jwt = extractJwt(authentication);
        if (jwt == null) {
            return false;
        }

        String subject = jwt.getSubject();
        if (subject == null) {
            return false;
        }

        try {
            UUID principalId = UUID.fromString(subject);
            return principalId.equals(userId);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean canAccessUser(Authentication authentication, UUID targetUserId) {
        return isSelf(authentication, targetUserId) || hasPermissionOrRole(authentication, "user:view", "ADMIN");
    }

    public boolean canUpdateUser(Authentication authentication, UUID targetUserId) {
        return isSelf(authentication, targetUserId) || hasPermissionOrRole(authentication, "user:update", "ADMIN");
    }

    public boolean canChangePassword(Authentication authentication, UUID userId) {
        return isSelf(authentication, userId);
    }

    public boolean canAssignRoles(Authentication authentication) {
        return hasPermissionOrRole(authentication, "user:assign-roles", "ADMIN");
    }

    public boolean isAdmin(Authentication authentication) {
        return hasRole(authentication, "ADMIN");
    }

    public boolean canCreateUsers(Authentication authentication) {
        return hasPermissionOrRole(authentication, "user:manage", "ADMIN");
    }

    public boolean canViewUsers(Authentication authentication) {
        return hasPermissionOrRole(authentication, "user:view", "ADMIN")
                || hasScope(authentication, "user:view");
    }

    public boolean canUpdateUsers(Authentication authentication) {
        return hasPermissionOrRole(authentication, "user:update", "ADMIN");
    }

    public boolean canDeleteUsers(Authentication authentication) {
        return hasPermissionOrRole(authentication, "user:delete", "ADMIN");
    }

    public boolean canAdminUpdateUsers(Authentication authentication) {
        return hasPermissionOrRole(authentication, "user:manage", "ADMIN");
    }

    public boolean canViewRoles(Authentication authentication) {
        return hasPermissionOrRole(authentication, "role:view", "ADMIN");
    }

    public boolean canManagePermissions(Authentication authentication) {
        return hasPermissionOrRole(authentication, "permission:create", "ADMIN")
                || hasPermissionOrRole(authentication, "permission:update", "ADMIN")
                || hasPermissionOrRole(authentication, "permission:delete", "ADMIN");
    }

    public boolean canChangePasswordFor(Authentication authentication, UUID targetUserId) {
        return isSelf(authentication, targetUserId) || hasRole(authentication, "ADMIN");
    }

    public boolean hasScope(Authentication authentication, String scope) {
        Jwt jwt = extractJwt(authentication);
        if (jwt == null) {
            return false;
        }

        List<String> scopes = jwt.getClaimAsStringList("scope");
        return scopes != null && scopes.contains(scope);
    }

    private Jwt extractJwt(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken();
        }

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt;
        }

        return null;
    }

    public boolean isInternalClient(Authentication authentication) {
        log.info("🔐 Проверка internal клиента: {}", authentication);

        Jwt jwt = extractJwt(authentication);
        if (jwt == null) {
            return false;
        }

        String clientId = jwt.getClaimAsString("client_id");
        log.info("🔐 client_id из JWT: {}", clientId);
        return "internal-service-client".equals(clientId);
    }

    private List<String> getClaimAsList(Jwt jwt, String claimName) {
        Object claim = jwt.getClaims().get(claimName);
        if (claim instanceof Collection<?> collection) {
            return collection.stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .toList();
        }
        return List.of();
    }
}


