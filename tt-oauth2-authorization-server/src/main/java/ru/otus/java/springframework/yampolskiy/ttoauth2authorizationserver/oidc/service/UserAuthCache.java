package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.oidc.service;

import org.springframework.stereotype.Component;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.integrations.users.dto.UserPrincipalDTO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserAuthCache {

    private final Map<String, UserPrincipalDTO> cache = new ConcurrentHashMap<>();

    public void put(String username, UserPrincipalDTO user) {
        cache.put(username, user);
    }

    public UserPrincipalDTO get(String username) {
        return cache.get(username);
    }

    public void remove(String username) {
        cache.remove(username);
    }
}
