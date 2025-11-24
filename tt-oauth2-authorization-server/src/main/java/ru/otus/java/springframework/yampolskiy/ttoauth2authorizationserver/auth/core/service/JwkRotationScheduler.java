package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwkRotationScheduler {

    private final JwkService jwkService;

    @Scheduled(cron = "0 0 0 1 * *") // 1-го числа каждого месяца в 00:00
    public void scheduledKeyRotation() {
        jwkService.rotateKey();
        System.out.println("✅ JWK ключ ротирован автоматически");
    }
}
