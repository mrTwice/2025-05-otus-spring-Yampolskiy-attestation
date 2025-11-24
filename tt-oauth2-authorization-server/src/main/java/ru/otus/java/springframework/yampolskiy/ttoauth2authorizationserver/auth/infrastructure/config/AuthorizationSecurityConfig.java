package ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationEndpointConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2TokenRevocationEndpointConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OidcConfigurer;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.infrastructure.logging.AuthorizationCodeRequestLogger;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.infrastructure.logging.TokenEndpointLoggingFilter;
import ru.otus.java.springframework.yampolskiy.ttoauth2authorizationserver.auth.core.service.DatabaseAuthorizationService;

import java.util.List;

@OpenAPIDefinition(
        info = @Info(
                title = "TT Auth Server",
                version = "1.0",
                description = "Authorization API"
        )
)
@Configuration
@RequiredArgsConstructor
public class AuthorizationSecurityConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationSecurityConfig.class);

    private final DatabaseAuthorizationService authorizationService;

    private final AuthenticationProvider loggingOidcUserInfoAuthenticationProvider;

    private final AuthenticationProvider clientAuthenticationProvider;

    private final Filter jwtDebugLogger;

    private final JwtDecoder jwtDecoder;

    private final AuthenticationProvider authorizationCodeAuthenticationProvider;

    private final AuthenticationProvider authorizationCodeRequestAuthenticationProvider;

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http
    ) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();

        configureHttp(
                http,
                authorizationServerConfigurer);

        return http.build();
    }

    private void configureHttp(
            HttpSecurity http,
            OAuth2AuthorizationServerConfigurer authorizationServerConfigurer
    ) throws Exception {
        http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authenticationProvider(clientAuthenticationProvider)
                .authenticationProvider(authorizationCodeRequestAuthenticationProvider)
                .authenticationProvider(authorizationCodeAuthenticationProvider)
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, getOAuth2AuthorizationServerConfigurerCustomizer(
                        authorizationService,
                        loggingOidcUserInfoAuthenticationProvider))
                .addFilterAfter(jwtDebugLogger, BearerTokenAuthenticationFilter.class)
                .oauth2ResourceServer(resourceServer ->
                        resourceServer.jwt(jwt -> jwt.decoder(jwtDecoder)))
                .authorizeHttpRequests((authorize) ->
                        authorize.anyRequest().authenticated())
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));
        http.addFilterBefore(new TokenEndpointLoggingFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    private Customizer<OAuth2AuthorizationServerConfigurer> getOAuth2AuthorizationServerConfigurerCustomizer(
            DatabaseAuthorizationService authorizationService,
            AuthenticationProvider loggingOidcUserInfoAuthenticationProvider) {
        return (authorizationServer) ->
                authorizationServer
                        .authorizationService(authorizationService)
                        .authorizationEndpoint(getOAuth2AuthorizationEndpointConfigurerCustomizer())
                        .oidc(getOidcConfigurerCustomizer(loggingOidcUserInfoAuthenticationProvider))
                        .tokenRevocationEndpoint(
                                getOAuth2TokenRevocationEndpointConfigurerCustomizer(authorizationService));
    }

    private Customizer<OAuth2TokenRevocationEndpointConfigurer> getOAuth2TokenRevocationEndpointConfigurerCustomizer(
            DatabaseAuthorizationService authorizationService
    ) {
        return tokenRevocationEndpointConfigurer ->
                tokenRevocationEndpointConfigurer
                        .revocationResponseHandler((request,
                                                    response,
                                                    authentication) -> {
                            String token = request.getParameter(OAuth2ParameterNames.TOKEN);
                            if (StringUtils.hasText(token)) {
                                OAuth2Authorization authorization
                                        = authorizationService.findByToken(
                                        token,
                                        null
                                );
                                if (authorization != null) {
                                    LOGGER.info(
                                            "Удаляем токен через кастомный revocationResponseHandler: {}",
                                            authorization.getId());
                                    authorizationService.remove(authorization);
                                } else {
                                    LOGGER.warn("Токен не найден для удаления: {}", token);
                                }
                            }
                            response.setStatus(HttpServletResponse.SC_OK);
                        });
    }

    private Customizer<OAuth2AuthorizationEndpointConfigurer> getOAuth2AuthorizationEndpointConfigurerCustomizer() {
        return authorizationEndpoint ->
                authorizationEndpoint.authorizationRequestConverter(
                        new AuthorizationCodeRequestLogger());
    }

    private Customizer<OidcConfigurer> getOidcConfigurerCustomizer(
            AuthenticationProvider loggingOidcUserInfoAuthenticationProvider
    ) {
        return oidc -> oidc
                .userInfoEndpoint(userInfo -> {
                    LOGGER.info("✅ Конфигурируем userInfoEndpoint");
                    userInfo.authenticationProvider(loggingOidcUserInfoAuthenticationProvider);
                });
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(
            HttpSecurity http,
            AuthenticationProvider userAuthenticationProvider
    ) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authenticationProvider(userAuthenticationProvider)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/oauth2/token").permitAll()
                        .requestMatchers("/api/v1/auth/register").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**"
                        ).permitAll().anyRequest().authenticated())
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/v1/auth/register", "/oauth2/token"))
                .formLogin(Customizer.withDefaults())
                .requestCache(requestCache -> requestCache
                        .requestCache(new HttpSessionRequestCache()))
                .exceptionHandling(exceptions ->
                        exceptions.accessDeniedHandler((
                                request,
                                response,
                                accessDeniedException
                        ) -> {
                            LOGGER.error("Ошибка доступа: {}", accessDeniedException.getMessage());
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                        }));
        return http.build();
    }

    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher() {
        return new AuthenticationEventPublisher() {
            private final Logger logger = LoggerFactory.getLogger("AuthenticationEventPublisher");

            @Override
            public void publishAuthenticationSuccess(Authentication authentication) {
                logger.info("✅ Успешная аутентификация: {}", authentication.getClass().getSimpleName());
            }

            @Override
            public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
                logger.warn("❌ Ошибка аутентификации: {}, тип токена: {}", exception.getMessage(),
                        authentication != null ? authentication.getClass().getSimpleName() : "null", exception);
            }
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(List.of(
                "http://localhost:8085",
                "http://localhost:9590",
                "http://localhost:9591"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/oauth2/token", config);
        source.registerCorsConfiguration("/oauth2/introspect", config);
        source.registerCorsConfiguration("/oauth2/revoke", config);
        source.registerCorsConfiguration("/oauth2/userinfo", config);
        source.registerCorsConfiguration("/oauth2/jwks", config);
        source.registerCorsConfiguration("/.well-known/openid-configuration", config);
        source.registerCorsConfiguration("/.well-known/oauth-authorization-server", config);
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }


}
