package com.example.api_gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterConfig {

    // Limits requests based on the client's source IP address
    @Bean
    KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }

    @Bean
    RedisRateLimiter redisRateLimiter() {
        // replenishRate: 10, burstCapacity: 20, requestedTokens: 1 (default)
        return new RedisRateLimiter(1, 1,1); 
    }

   
}