package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.oidc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.integrations.exceptions.IntegrationException;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.integrations.users.client.UserProfileClient;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.integrations.users.dto.UserPrincipalDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class OidcUserInfoService {

    //TODO подумать о переносе сервиса в external/users
    private final UserProfileClient userProfileClient;

    public OidcUserInfo loadUser(String username) {
        try {
            UserPrincipalDTO user = userProfileClient.findByUsername(username);

            Map<String, Object> claims = createClaims(user);

            return new OidcUserInfo(claims);

        } catch (IntegrationException e) {
            if ("USER_NOT_FOUND".equals(e.getCode())) {
                log.warn("OIDC: Пользователь не найден: {}", username);
                throw new UsernameNotFoundException("User not found: " + username);
            }

            log.error("OIDC: Ошибка при получении профиля пользователя", e);
            throw new InternalAuthenticationServiceException("UserService unavailable", e);
        } catch (Exception e) {
            log.error("OIDC: Непредвиденная ошибка при загрузке профиля: {}", username, e);
            throw new InternalAuthenticationServiceException("Unexpected error", e);
        }
    }

    private Map<String, Object> createClaims(UserPrincipalDTO user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", user.getOidcSubject());
        putIfNotNull(claims, "name", fullName(user));
        putIfNotNull(claims, "given_name", user.getFirstName());
        putIfNotNull(claims, "middle_name", user.getMiddleName());
        putIfNotNull(claims, "family_name", user.getLastName());
        putIfNotNull(claims, "nickname", user.getNickname());
        putIfNotNull(claims, "preferred_username", user.getUsername());
        putIfNotNull(claims, "profile", toStringOrNull(user.getProfile()));
        putIfNotNull(claims, "picture", toStringOrNull(user.getPictureUrl()));
        putIfNotNull(claims, "website", toStringOrNull(user.getWebsite()));
        putIfNotNull(claims, "email", user.getEmail());
        claims.put("email_verified", user.isEmailVerified());
        putIfNotNull(claims, "gender", user.getGender());
        putIfNotNull(claims, "birthdate", user.getBirthdate() != null ? user.getBirthdate().toString() : null);
        putIfNotNull(claims, "zoneinfo", user.getZoneinfo());
        putIfNotNull(claims, "locale", user.getLocale());
        putIfNotNull(claims, "phone_number", user.getPhoneNumber());
        claims.put("phone_number_verified", user.isPhoneNumberVerified());
        putIfNotNull(claims, "address", user.getAddress());
        putIfNotNull(
                claims, "updated_at", user.getUpdatedAtOidc() != null ? user.getUpdatedAtOidc().getEpochSecond() : null);
        return claims;
    }

    private String fullName(UserPrincipalDTO user) {
        return Stream.of(user.getFirstName(), user.getMiddleName(), user.getLastName())
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
    }

    private void putIfNotNull(Map<String, Object> map, String key, Object value) {
        if (value != null) {
            map.put(key, value);
        }
    }

    private String toStringOrNull(Object obj) {
        return obj != null ? obj.toString() : null;
    }
}


