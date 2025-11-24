package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.oidc.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.oidc.service.OidcUserInfoService;

@Configuration
@Slf4j
public class IdTokenCustomizerConfig {
    // TODO: Добавить условную логику
    //  - кастомизировать клеймы в зависимости от клиента, grant_type, scope и т.д.
    // Пример: пропустить ID токен, если он не из authorization_code flow
    // TODO: Подгрузка дополнительных данных из внешних микросервисов (например, профиля, ролей, статуса)
    // TODO: Вставка стандартных и кастомных OIDC-клеймов
    // TODO: Добавить поддержку клеймов: auth_time, acr, amr и т.п. — для соответствия OIDC спецификации
    // TODO: Добавление кастомных бизнес-клеймов, если нужно передавать роли, идентификаторы и пр.
    // TODO: Логгирование содержимого ID токена для отладки

    // TODO: Делегируемая архитектура
    //  - реализовать интерфейс IdTokenClaimContributor и подключать несколько бинов
    // Пример: создать список бинов и вызывать contributors.forEach(c -> c.contribute(context, claims));
    // Разбить кастомизацию на модули (например, StandardClaimsCustomizer, ProfileClaimsCustomizer)
    // Или внедрить механизм регистрации TokenClaimContributor и вызывать их по очереди.
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> idTokenCustomizer(
            OidcUserInfoService userInfoService) {
        log.info("🛠 idTokenCustomizer initializing ");
        return (context) -> {
            if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
                log.info("🎯 Customizing idtoken for client: {}", context.getRegisteredClient().getClientId());


                OidcUserInfo userInfo = userInfoService.loadUser(
                        context.getPrincipal().getName());
                context.getClaims().claims(claims ->
                        claims.putAll(userInfo.getClaims()));
            }
        };
    }
}
