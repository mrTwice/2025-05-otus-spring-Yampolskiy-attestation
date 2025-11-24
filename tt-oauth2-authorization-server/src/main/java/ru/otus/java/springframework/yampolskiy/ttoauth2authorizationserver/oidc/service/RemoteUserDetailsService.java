package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.oidc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.integrations.exceptions.IntegrationException;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.integrations.users.client.UserProfileClient;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.integrations.users.dto.UserPrincipalDTO;

@Service
@RequiredArgsConstructor
@Slf4j
public class RemoteUserDetailsService implements UserDetailsService {

    //TODO подумать о переносе сервиса в external/users
    private final UserProfileClient userProfileClient;

    private final UserAuthCache userAuthCache;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("🔐 RemoteUserDetailsService.loadUserByUsername called for: {}", username);
        try {
            UserPrincipalDTO user = userProfileClient.findByUsername(username);
            userAuthCache.put(username, user);
            log.debug("👤 DTO от user-service: {}", user);
            return user;
        } catch (IntegrationException e) {
            switch (e.getCode()) {
                case "USER_NOT_FOUND" -> {
                    log.warn("🔒 Пользователь не найден в User-Service: {}", username);
                    throw new UsernameNotFoundException("User not found: " + username, e);
                }
                case "USER_SERVICE_UNAUTHORIZED" -> {
                    log.error("🔑 User-Service вернул 401 (проверь сервисную авторизацию auth-server → user-service)", e);
                    throw new InternalAuthenticationServiceException(
                            "User-Service returned 401 (service-to-service auth failed)", e);
                }
                default -> {
                    log.error("💥 Ошибка User-Service при загрузке пользователя: {}", username, e);
                    throw new InternalAuthenticationServiceException("UserService unavailable", e);
                }
            }
        }
    }
}