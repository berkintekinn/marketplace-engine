package com.marketplace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marketplace.dto.AuthRequest;
import com.marketplace.dto.AuthResponse;
import com.marketplace.dto.RegisterRequest;
import com.marketplace.services.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired 
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        String token = authService.register(
            request.name(), 
            request.email(), 
            request.password(), 
            request.role()
        );
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        String token = authService.login(request.email(), request.password());
        return ResponseEntity.ok(new AuthResponse(token));
}
}