package com.example.sistemagestion.service;

import com.example.sistemagestion.dto.auth.AuthResponse;
import com.example.sistemagestion.dto.auth.LoginRequest;
import com.example.sistemagestion.dto.auth.RegisterRequest;
import com.example.sistemagestion.dto.auth.TokenValidationRequest;
import com.example.sistemagestion.model.Role;
import com.example.sistemagestion.model.Session;
import com.example.sistemagestion.model.Usuario;
import com.example.sistemagestion.repository.RoleRepository;
import com.example.sistemagestion.repository.SessionRepository;
import com.example.sistemagestion.repository.UsuarioRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final SessionService sessionService;
    private final SessionRepository sessionRepository;


    // MÉTODO REGISTER
    public String register(RegisterRequest request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }

        Role roleUser = roleRepository.findByNombre("USER")
                .orElseThrow(() -> new RuntimeException("Error: Rol USER no encontrado."));

        var user = Usuario.builder()
                .username(request.getUsername())
                .apellido(request.getApellido())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(roleUser)
                .build();

        usuarioRepository.save(user);

        return "Te has registrado correctamente";
    }

    // MÉTODO LOGIN
    public AuthResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        var user = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        sessionService.createSession(user, accessToken, refreshToken, httpRequest);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // MÉTODO VALIDATE TOKENS
    public Map<String, Object> validateTokens(TokenValidationRequest request) {
        Map<String, Object> response = new HashMap<>();

        // BUSCAR LA SESIÓN POR REFRESH TOKEN EN LA BD
        Session session = sessionRepository.findByRefreshToken(request.getRefresh_token())
                .orElse(null);

        if (session == null) {
            response.put("valid", false);
            response.put("message", "Sesión no válida o revocada (No encontrada en BD)");
            return response;
        }

        ZoneId zonePeru = ZoneId.of("America/Lima");
        LocalDateTime ahora = LocalDateTime.now(zonePeru);

        // VERIFICAR SI EL REFRESH TOKEN YA VENCIÓ
        if (session.getRefreshExpiresAt().isBefore(ahora)) {
            sessionRepository.delete(session);
            response.put("valid", false);
            response.put("message", "Sesión expirada (Refresh token vencido)");
            return response;
        }

        //  VERIFICAR DISCREPANCIA
        if (!session.getAccessToken().equals(request.getAccess_token())) {
            response.put("valid", false);
            response.put("message", "Discrepancia de tokens (Access token no coincide)");
            return response;
        }

        //  VALIDAR EL ACCESS TOKEN
        try {
            jwtService.extractUsername(request.getAccess_token());
            response.put("valid", true);
            response.put("message", "OK, tokens válidos");

        } catch (ExpiredJwtException e) {
            // RENOVACIÓN AUTOMÁTICA
            var user = session.getUsuario();
            String newAccessToken = jwtService.generateAccessToken(user);

            session.setAccessToken(newAccessToken);
            session.setAccessExpiresAt(ahora.plusMinutes(5));
            sessionRepository.save(session);

            response.put("valid", true);
            response.put("message", "Access token renovado");
            response.put("access_token", newAccessToken);

        } catch (Exception e) {
            response.put("valid", false);
            response.put("message", "Error al validar token: " + e.getMessage());
        }

        return response;
    }
}