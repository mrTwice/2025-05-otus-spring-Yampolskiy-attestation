package ru.otus.java.springframework.yampolskiy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class GatewaySecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)

                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/actuator/health/**").permitAll()
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .pathMatchers("/api/v1/auth/**").permitAll()
                        .pathMatchers(
                                "/user/swagger-ui/**", "/user/v3/api-docs/**", "/user/v3/api-docs.yaml",
                                "/task/swagger-ui/**", "/task/v3/api-docs/**", "/task/v3/api-docs.yaml",
                                "/sas/swagger-ui/**", "/sas/v3/api-docs/**", "/sas/v3/api-docs.yaml"
                        ).permitAll()
                        .pathMatchers("/oauth2/**").permitAll()
                        .pathMatchers("/sas/oauth2/**").permitAll()
                        .pathMatchers("/login").permitAll()
                        .pathMatchers("/default-ui.css").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "http://localhost:8080",
                "http://localhost:8085",
                "null"
        ));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/oauth2/token", config);
        source.registerCorsConfiguration("/oauth2/revoke", config);
        source.registerCorsConfiguration("/oauth2/introspect", config);
        source.registerCorsConfiguration("/oauth2/userinfo", config);
        source.registerCorsConfiguration("/oauth2/jwks", config);

        source.registerCorsConfiguration("/api/**", config);

        return new CorsWebFilter(source);
    }
}
