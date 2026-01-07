package com.example.sistemagestion.service;

import com.example.sistemagestion.dto.usuario.CreateUserDto;
import com.example.sistemagestion.dto.usuario.UpdateUserDto;
import com.example.sistemagestion.dto.usuario.UserResponseDto;
import com.example.sistemagestion.model.Role;
import com.example.sistemagestion.model.Usuario;
import com.example.sistemagestion.repository.RoleRepository;
import com.example.sistemagestion.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // LISTAR TODOS
    public List<UserResponseDto> getAllUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // OBTENER UNO
    public UserResponseDto getUsuarioById(Integer id) {
        Usuario user = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return mapToDto(user);
    }

    // CREAR USUARIO
    public String createUsuario(CreateUserDto request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        Role role = roleRepository.findByNombre(request.getRole().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Rol no válido (Use ADMIN o USER)"));

        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .apellido(request.getApellido())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .enabled(true)
                .accountNonLocked(true)
                .build();

        usuarioRepository.save(usuario);

        // RETORNAMOS SOLO EL MENSAJE
        return "Usuario creado correctamente";
    }

    // ACTUALIZAR USUARIO
    public String updateUsuario(Integer id, UpdateUserDto request) {
        Usuario user = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setUsername(request.getUsername());
        user.setApellido(request.getApellido());
        user.setEmail(request.getEmail());

        if (request.getRole() != null && !request.getRole().isEmpty()) {
            Role role = roleRepository.findByNombre(request.getRole().toUpperCase())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            user.setRole(role);
        }

        usuarioRepository.save(user);

        // RETORNAMOS SOLO EL MENSAJE
        return "Usuario actualizado correctamente";
    }

    // BORRADO LÓGICO
    public String deleteUsuarioLogico(Integer id) {
        Usuario user = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setEnabled(false);
        usuarioRepository.save(user);

        // RETORNAMOS SOLO EL MENSAJE
        return "Usuario eliminado correctamente";
    }

    // Método auxiliar
    private UserResponseDto mapToDto(Usuario user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .apellido(user.getApellido())
                .email(user.getEmail())
                .role(user.getRole().getNombre())
                .enabled(user.isEnabled())
                .build();
    }
}