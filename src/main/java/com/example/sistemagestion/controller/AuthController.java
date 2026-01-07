package com.example.sistemagestion.controller;

import com.example.sistemagestion.dto.auth.AuthResponse;
import com.example.sistemagestion.dto.auth.LoginRequest;
import com.example.sistemagestion.dto.auth.RegisterRequest;
import com.example.sistemagestion.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor // Esto crea el constructor para todos los campos 'final'
public class AuthController {

    // 1. DECLARAR TODAS LAS DEPENDENCIAS AQUÍ
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest request) {
        String mensaje = authService.register(request);
        return ResponseEntity.ok(Collections.singletonMap("message", mensaje));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        AuthResponse response = authService.login(loginRequest, request);

        return ResponseEntity.ok(response);
    }
}