package com.example.api_gateway.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.api_gateway.security.AuthenticationFilter;


@Configuration
public class GatewayConfig {

	@Autowired
	private AuthenticationFilter authFilter;
	
	@Bean
    RouteLocator customRouteLocator(RouteLocatorBuilder builder,
                                 RedisRateLimiter redisRateLimiter,
                                 KeyResolver ipKeyResolver) {
        System.out.println("========================================");
        System.out.println("CREATING GATEWAY ROUTES");
        System.out.println("========================================");
        
        return builder.routes()
                // Auth Service Route - NO AUTHENTICATION (PUBLIC)
                .route("auth-service", r -> {
                    System.out.println("Adding route: auth-service -> lb://AUTH-SERVICE (PUBLIC - NO AUTH)");
                    return r.path("/auth/**")  // Keep as /auth/**
                            .uri("lb://AUTH-SERVICE");
                })
                
                // Product Service Route - WITH AUTHENTICATION & RATE LIMITING
                .route("product-service", r -> {
                    System.out.println("Adding route: product-service -> lb://PRODUCT-SERVICE (WITH AUTH)");
                    return r.path("/api/products/**")
                            .filters(f -> f
                            		.filter(authFilter.apply(new AuthenticationFilter.Config()))
                                    .requestRateLimiter(config -> config
                                            .setRateLimiter(redisRateLimiter)
                                            .setKeyResolver(ipKeyResolver)
                                    ))
                            .uri("lb://PRODUCT-SERVICE");
                })
                
                // Cart Service Route - WITH AUTHENTICATION
                .route("cart-service", r -> {
                    System.out.println("Adding route: cart-service -> lb://CART-SERVICE (WITH AUTH)");
                    return r.path("/api/cart/**")
                    		.filters(f -> f.filter(authFilter.apply(new AuthenticationFilter.Config())))
                            .uri("lb://CART-SERVICE");
                })
                
                // Order Service Route - WITH AUTHENTICATION
                .route("order-service", r -> {
                    System.out.println("Adding route: order-service -> lb://ORDER-SERVICE (WITH AUTH)");
                    return r.path("/api/orders/**")
                    		.filters(f -> f.filter(authFilter.apply(new AuthenticationFilter.Config())))
                            .uri("lb://ORDER-SERVICE");
                })
                
                .build();
    }
}