package com.example.order_service.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class HeaderAuthenticationFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) 
            throws ServletException, IOException {
        
        // Extract headers added by Gateway
        String userId = request.getHeader("X-User-Id");
        String email = request.getHeader("X-User-Email");
        String rolesHeader = request.getHeader("X-User-Roles");
        String permissionsHeader = request.getHeader("X-User-Permissions");
        
        if (email != null && rolesHeader != null) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            
            // Add roles with ROLE_ prefix
            if (rolesHeader != null && !rolesHeader.isEmpty()) {
                Arrays.stream(rolesHeader.split(","))
                        .forEach(role -> authorities.add(
                            new SimpleGrantedAuthority("ROLE_" + role.trim())));
            }
            
            // Add permissions as-is
            if (permissionsHeader != null && !permissionsHeader.isEmpty()) {
                Arrays.stream(permissionsHeader.split(","))
                        .forEach(perm -> authorities.add(
                            new SimpleGrantedAuthority(perm.trim())));
            }
            
            // Create custom principal with user info
            UserPrincipal principal = new UserPrincipal(
                Long.parseLong(userId), email, authorities);
            
            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(principal, null, authorities);
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        
        filterChain.doFilter(request, response);
    }
}