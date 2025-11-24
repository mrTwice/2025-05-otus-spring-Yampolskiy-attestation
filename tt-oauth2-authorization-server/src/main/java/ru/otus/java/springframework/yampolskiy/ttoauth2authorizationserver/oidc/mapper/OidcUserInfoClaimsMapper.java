package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.oidc.mapper;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationProvider;
import org.springframework.stereotype.Component;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.oidc.service.OidcUserInfoService;

@Component
@RequiredArgsConstructor

public class OidcUserInfoClaimsMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(OidcUserInfoClaimsMapper.class);

    private final OidcUserInfoService oidcUserInfoService;

    public void configureUserInfoMapper(OidcUserInfoAuthenticationProvider provider) {
        LOGGER.info("Configuring user info mapper");
        provider.setUserInfoMapper(this::mapUserInfo);
    }

    private OidcUserInfo mapUserInfo(OidcUserInfoAuthenticationContext context) {
        String username = context.getAuthorization().getPrincipalName();
        return oidcUserInfoService.loadUser(username);
    }
}
