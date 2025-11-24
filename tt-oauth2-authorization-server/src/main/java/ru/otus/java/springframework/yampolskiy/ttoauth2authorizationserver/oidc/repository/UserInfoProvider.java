package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.oidc.repository;

import java.util.Map;

public interface UserInfoProvider {
    Map<String, Object> getClaimsByUsername(String username);
}
