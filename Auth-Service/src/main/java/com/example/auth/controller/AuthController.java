package com.example.auth.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.events.LoginEvent;

import lombok.RequiredArgsConstructor;

import com.example.auth.dto.AuthResponse;
import com.example.auth.dto.LoginRequest;
import com.example.auth.dto.RegisterRequest;
import com.example.auth.dto.TokenValidationResponse;
import com.example.auth.entity.User;
import com.example.auth.repository.UserRepository;
import com.example.auth.security.JwtUtil;
import com.example.auth.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    
   
    private final AuthService authService;
    private final KafkaTemplate<String, LoginEvent> kafkaTemplate;
    
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
    	// ✅ CREATE EVENT LOCALLY
    	LoginEvent event = new LoginEvent(
    	        request.getEmail(),
    	        request.getUsername(),
    	        LocalDateTime.now()
    	);

    	// ✅ PRINT EVENT PROPERLY
    	System.out.println("LOGIN EVENT => " + event);
    	System.out.println("type of event is " + event.getClass().getName());
    		

    	// ✅ SEND TO KAFKA
    	kafkaTemplate.send("user-login-topic", event);
        return ResponseEntity.ok(authService.login(request));
    }
    
    @PostMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validateToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Remove "Bearer "
        return ResponseEntity.ok(authService.validateToken(token));
    }
}


