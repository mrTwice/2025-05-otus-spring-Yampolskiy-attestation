package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.oidc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.core.service.LoggingDaoAuthenticationProvider;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.oidc.service.RemoteUserDetailsService;

@Configuration
public class OidcAuthenticationConfig {

    @Bean
    public AuthenticationProvider userAuthenticationProvider(
            RemoteUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        return new LoggingDaoAuthenticationProvider(userDetailsService, passwordEncoder);
    }

}