package com.example.sistemagestion.controller;

import com.example.sistemagestion.dto.usuario.CreateUserDto;
import com.example.sistemagestion.dto.usuario.UpdateUserDto;
import com.example.sistemagestion.dto.usuario.UserResponseDto;
import com.example.sistemagestion.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "CRUD de Usuarios (Solo ADMIN)")
public class UsuarioController {

    private final UsuarioService usuarioService;

    // GETs
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Listar todos los usuarios", description = "Devuelve una lista completa de usuarios")
    public ResponseEntity<List<UserResponseDto>> getAll() {
        return ResponseEntity.ok(usuarioService.getAllUsuarios());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Obtener usuario por ID")
    public ResponseEntity<UserResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.getUsuarioById(id));
    }

    // POST: Crear usuario
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Crear Usuario", description = "Crea un usuario con rol específico (ADMIN o USER)")
    public ResponseEntity<Map<String, String>> create(@RequestBody CreateUserDto request) {
        String mensaje = usuarioService.createUsuario(request);
        return ResponseEntity.ok(Collections.singletonMap("message", mensaje));
    }

    // PUT: Actualizar usuario
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Actualizar Usuario", description = "Actualiza datos básicos y rol")
    public ResponseEntity<Map<String, String>> update(@PathVariable Integer id, @RequestBody UpdateUserDto request) {
        String mensaje = usuarioService.updateUsuario(id, request);
        return ResponseEntity.ok(Collections.singletonMap("message", mensaje));
    }

    // DELETE: Borrado lógico
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Eliminar Usuario (Lógico)", description = "Deshabilita el usuario sin borrarlo de la BD")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Integer id) {
        String mensaje = usuarioService.deleteUsuarioLogico(id);
        return ResponseEntity.ok(Collections.singletonMap("message", mensaje));
    }
}