package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.api.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.Set;

@Getter
@Builder
public class TokenIntrospectionResponse {

    private boolean active;

    private String clientId;

    private Set<String> scopes;

    private Instant expiresAt;
}
