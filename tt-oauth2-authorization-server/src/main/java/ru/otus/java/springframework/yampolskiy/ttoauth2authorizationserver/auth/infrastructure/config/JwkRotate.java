package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.core.service.JwkService;

@Component
@RequiredArgsConstructor
public class JwkRotate implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwkRotate.class);

    private final JwkService jwkService;

    @Override
    public void run(String... args) {
        LOGGER.info("🚀 Bootstrap JwkRotate");
        jwkService.rotateKey();
        LOGGER.info("🚀 All keys was rotated");
    }
}