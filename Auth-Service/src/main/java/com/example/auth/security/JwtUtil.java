package com.example.auth.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.auth.entity.Permission;
import com.example.auth.entity.Role;
import com.example.auth.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;




import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

	 @Value("${jwt.secret}")
	    private String jwtSecret;
	    
	    @Value("${jwt.expiration:86400000}") // 24 hours default
	    private Long jwtExpiration;
	    
	    public String generateToken(User user) {
	        Map<String, Object> claims = new HashMap<>();
	        
	        // Extract roles
	        List<String> roles = user.getRoles().stream()
	                .map(Role::getName)
	                .collect(Collectors.toList());
	        
	        // Extract permissions (from all roles)
	        List<String> permissions = user.getRoles().stream()
	                .flatMap(role -> role.getPermissions().stream())
	                .map(Permission::getName)
	                .distinct()
	                .collect(Collectors.toList());
	        
	        claims.put("roles", roles);
	        claims.put("permissions", permissions);
	        claims.put("userId", user.getId());
	        claims.put("email", user.getEmail());
	        claims.put("firstName", user.getFirstName());
	        claims.put("lastName", user.getLastName());
	        
	        return Jwts.builder()
	                .setClaims(claims)
	                .setSubject(user.getEmail())
	                .setIssuedAt(new Date())
	                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
	                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS256)
	                .compact();
	    }
	    
	    public String extractUsername(String token) {
	        return extractClaim(token, Claims::getSubject);
	    }
	    
	    public List<String> extractRoles(String token) {
	        Claims claims = extractAllClaims(token);
	        return claims.get("roles", List.class);
	    }
	    
	    public List<String> extractPermissions(String token) {
	        Claims claims = extractAllClaims(token);
	        return claims.get("permissions", List.class);
	    }
	    
	    public Long extractUserId(String token) {
	        Claims claims = extractAllClaims(token);
	        return claims.get("userId", Long.class);
	    }
	    
	    public boolean validateToken(String token) {
	        try {
	            Jwts.parserBuilder()
	                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
	                    .build()
	                    .parseClaimsJws(token);
	            return true;
	        } catch (JwtException | IllegalArgumentException e) {
	            return false;
	        }
	    }
	    
	    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
	        final Claims claims = extractAllClaims(token);
	        return claimsResolver.apply(claims);
	    }
	    
	    private Claims extractAllClaims(String token) {
	        return Jwts.parserBuilder()
	                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
	                .build()
	                .parseClaimsJws(token)
	                .getBody();
	    }
}
