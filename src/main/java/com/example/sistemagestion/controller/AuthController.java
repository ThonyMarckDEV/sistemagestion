package com.example.sistemagestion.controller;

import com.example.sistemagestion.dto.auth.AuthResponse;
import com.example.sistemagestion.dto.auth.LoginRequest;
import com.example.sistemagestion.dto.auth.RegisterRequest;
import com.example.sistemagestion.dto.auth.TokenValidationRequest;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints para Login, Registro y Tokens")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario", description = "Crea un usuario con rol USER por defecto")
    @SecurityRequirements() // PÚBLICO
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest request) {
        String mensaje = authService.register(request);
        return ResponseEntity.ok(Collections.singletonMap("message", mensaje));
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar Sesión", description = "Devuelve Access Token y Refresh Token")
    @SecurityRequirements() // PÚBLICO
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        AuthResponse response = authService.login(loginRequest, request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate-tokens")
    @Operation(summary = "Validar/Renovar Tokens", description = "Valida el Refresh Token. Si el Access Token venció, lo renueva.")
    @SecurityRequirements() // PÚBLICO
    public ResponseEntity<Map<String, Object>> validateTokens(@RequestBody TokenValidationRequest request) {

        Map<String, Object> resultado = authService.validateTokens(request);

        // Si valid es false, devolvemos 401, si es true devolvemos 200
        boolean isValid = (boolean) resultado.get("valid");

        if (isValid) {
            return ResponseEntity.ok(resultado);
        } else {
            return ResponseEntity.status(401).body(resultado);
        }
    }
}