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

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // GETs
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAll() {
        return ResponseEntity.ok(usuarioService.getAllUsuarios());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.getUsuarioById(id));
    }

    // POST: Crear usuario
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, String>> create(@RequestBody CreateUserDto request) {
        String mensaje = usuarioService.createUsuario(request);
        return ResponseEntity.ok(Collections.singletonMap("message", mensaje));
    }

    // PUT: Actualizar usuario
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, String>> update(@PathVariable Integer id, @RequestBody UpdateUserDto request) {
        String mensaje = usuarioService.updateUsuario(id, request);
        return ResponseEntity.ok(Collections.singletonMap("message", mensaje));
    }

    // DELETE: Borrado lógico
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Integer id) {
        String mensaje = usuarioService.deleteUsuarioLogico(id);
        return ResponseEntity.ok(Collections.singletonMap("message", mensaje));
    }
}