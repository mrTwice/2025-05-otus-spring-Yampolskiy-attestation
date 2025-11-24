package ru.otus.java.springframework.yampolskiy.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ForwardedHeadersLoggingGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerWebExchange mutatedExchange = withForwardedHeaders(exchange);
        logRequest(mutatedExchange);
        return chain.filter(mutatedExchange);
    }

    private ServerWebExchange withForwardedHeaders(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();

        String host = headers.getFirst(HttpHeaders.HOST);
        String scheme = request.getURI().getScheme();
        int port = request.getURI().getPort();

        ServerHttpRequest.Builder builder = request.mutate();
        addIfAbsent(builder, headers, "X-Forwarded-Host", host);
        addIfAbsent(builder, headers, "X-Forwarded-Proto", scheme);
        addIfAbsent(builder, headers, "X-Forwarded-Port",
                port != -1 ? String.valueOf(port) : null);

        ServerHttpRequest mutatedRequest = builder.build();
        return exchange.mutate().request(mutatedRequest).build();
    }

    private void addIfAbsent(
            ServerHttpRequest.Builder builder,
            HttpHeaders headers,
            String headerName,
            @Nullable String value
    ) {
        if (!headers.containsKey(headerName) && value != null) {
            builder.header(headerName, value);
        }
    }

    private void logRequest(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();

        String routeId = exchange.getAttributeOrDefault(
                "springCloudGatewayRouteId", "unknown"
        );

        log.warn("=== [GW REQUEST] {} {} (routeId={}) ===",
                request.getMethod(), request.getURI(), routeId);
        log.warn("Host              : {}", headers.getFirst(HttpHeaders.HOST));
        log.warn("X-Forwarded-Host  : {}", headers.getFirst("X-Forwarded-Host"));
        log.warn("X-Forwarded-Proto : {}", headers.getFirst("X-Forwarded-Proto"));
        log.warn("X-Forwarded-Port  : {}", headers.getFirst("X-Forwarded-Port"));
        log.warn("Forwarded         : {}", headers.getFirst("Forwarded"));
    }



    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 10;
    }
}
