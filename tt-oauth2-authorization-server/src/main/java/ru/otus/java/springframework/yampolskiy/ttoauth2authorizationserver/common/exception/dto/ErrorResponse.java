package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.common.exception.dto;

import java.time.Instant;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path
) {}
