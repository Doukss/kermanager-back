package com.immo.gateway.filter;
import com.immo.common.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {
    private final JwtUtil jwtUtil;
    private static final String BEARER = "Bearer ";
    public JwtAuthFilter(JwtUtil jwtUtil) { this.jwtUtil = jwtUtil; }
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        if (path.startsWith("/auth/")) return chain.filter(exchange);
        String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(BEARER)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        try {
            Claims claims = jwtUtil.parse(header.substring(7));
            return chain.filter(exchange.mutate()
                .request(r -> r.header("X-Tenant-ID", claims.get("tenantId", String.class))
                                .header("X-User-Id",   claims.getSubject())
                                .header("X-Role",      claims.get("role", String.class)))
                .build());
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
    @Override public int getOrder() { return -1; }
}
