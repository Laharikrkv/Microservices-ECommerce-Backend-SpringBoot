package com.example.auth.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.auth.dto.AuthResponse;
import com.example.auth.dto.LoginRequest;
import com.example.auth.dto.RegisterRequest;
import com.example.auth.dto.TokenValidationResponse;
import com.example.auth.entity.Role;
import com.example.auth.entity.User;
import com.example.auth.repository.RoleRepository;
import com.example.auth.repository.UserRepository;
import com.example.auth.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {
    
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtTokenProvider;
    
    public String register(RegisterRequest request) {
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        
        // Assign default USER role
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.getRoles().add(userRole);
        
        user = userRepository.save(user);
        
        
        return user.getFirstName()+","+ "Registered Successfully";
    }
    
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        
        String token = jwtTokenProvider.generateToken(user);
        
        return new AuthResponse(token, user.getEmail(), 
                user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
    }
    
    public TokenValidationResponse validateToken(String token) {
        boolean isValid = jwtTokenProvider.validateToken(token);
        
        if (!isValid) {
            return new TokenValidationResponse(false, null, null, null, null);
        }
        
        String username = jwtTokenProvider.extractUsername(token);
        List<String> roles = jwtTokenProvider.extractRoles(token);
        List<String> permissions = jwtTokenProvider.extractPermissions(token);
        Long userId = jwtTokenProvider.extractUserId(token);
        
        return new TokenValidationResponse(true, username, roles, permissions, userId);
    }
}
