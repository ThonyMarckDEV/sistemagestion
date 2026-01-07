package com.example.sistemagestion.service;

import com.example.sistemagestion.dto.auth.AuthResponse;
import com.example.sistemagestion.dto.auth.LoginRequest;
import com.example.sistemagestion.dto.auth.RegisterRequest;
import com.example.sistemagestion.model.Role;
import com.example.sistemagestion.model.Usuario;
import com.example.sistemagestion.repository.RoleRepository;
import com.example.sistemagestion.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final SessionService sessionService;


    // MÉTODO REGISTER (Con validación de email repetido)
    public String register(RegisterRequest request) {

        // 1. Validar si el email ya existe antes de intentar guardar
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

    // MÉTODO LOGIN (Ahora maneja toda la lógica)
    public AuthResponse login(LoginRequest request, HttpServletRequest httpRequest) {

        // 1. Autenticar
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 2. Obtener usuario
        var user = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 3. Generar tokens
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        // 4. GUARDAR SESIÓN (Ahora sí funcionará porque inyectamos sessionService arriba)
        sessionService.createSession(user, accessToken, refreshToken, httpRequest);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}