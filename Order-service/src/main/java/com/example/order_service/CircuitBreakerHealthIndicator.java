package com.example.order_service;

import org.springframework.stereotype.Component;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

@Component
public class CircuitBreakerHealthIndicator implements org.springframework.boot.actuate.health.HealthIndicator {

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public CircuitBreakerHealthIndicator(CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    @Override
    public org.springframework.boot.actuate.health.Health health() {
        var healthBuilder = org.springframework.boot.actuate.health.Health.up();
        
        circuitBreakerRegistry.getAllCircuitBreakers().forEach(circuitBreaker -> {
            var metrics = circuitBreaker.getMetrics();
            var state = circuitBreaker.getState();
            
            healthBuilder.withDetail(circuitBreaker.getName(), java.util.Map.of(
                "state", state.toString(),
                "failureRate", String.format("%.2f%%", metrics.getFailureRate()),
                "slowCallRate", String.format("%.2f%%", metrics.getSlowCallRate()),
                "bufferedCalls", metrics.getNumberOfBufferedCalls(),
                "failedCalls", metrics.getNumberOfFailedCalls(),
                "successfulCalls", metrics.getNumberOfSuccessfulCalls(),
                "notPermittedCalls", metrics.getNumberOfNotPermittedCalls()
            ));
            
            // Mark as DOWN if circuit is OPEN
            if (state == io.github.resilience4j.circuitbreaker.CircuitBreaker.State.OPEN) {
                healthBuilder.down();
            }
        });
        
        return healthBuilder.build();
    }
}
