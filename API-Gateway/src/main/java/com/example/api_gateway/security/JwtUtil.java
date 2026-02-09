package com.example.api_gateway.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public Claims claims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            claims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return claims(token).getSubject();
    }

    public Long extractUserId(String token) {
        return claims(token).get("userId", Long.class);
    }

    public List<String> extractRoles(String token) {
        return claims(token).get("roles", List.class);
    }

    public List<String> extractPermissions(String token) {
        return claims(token).get("permissions", List.class);
    }


}
