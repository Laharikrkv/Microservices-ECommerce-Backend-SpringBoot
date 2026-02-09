	
	package com.example.api_gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> implements Ordered {

	private final JwtUtil jwtUtil;

    // Constructor injection is cleaner and safer than @Autowired on fields
    public AuthenticationFilter(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }
    public static class Config {}
    @Override
    public int getOrder() {
        return -1; // Makes this filter run BEFORE other internal filters
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // 1. Check if Authorization header is present
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing Authorization Header");
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Authorization Header");
            }

            String token = authHeader.substring(7);

            // 2. Validate Token using your JwtUtil
            try {
                if (!jwtUtil.validateToken(token)) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT Token");
                }
                
                // 3. Optional: Mutate request to add user details as headers for downstream
                ServerHttpRequest request = exchange.getRequest().mutate()
                        .header("X-User-Id", String.valueOf(jwtUtil.extractUserId(token)))
                        .header("X-User-Roles", String.join(",",jwtUtil.extractRoles(token)))
                        .header("X-User-Permissions", String.join(",", jwtUtil.extractPermissions(token)))
                        .build();

                return chain.filter(exchange.mutate().request(request).build());

            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token Validation Failed");
            }
        };
    }
}

