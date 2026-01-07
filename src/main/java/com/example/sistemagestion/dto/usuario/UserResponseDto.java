package com.example.sistemagestion.dto.usuario;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {
    private Integer id;
    private String username;
    private String apellido;
    private String email;
    private String role;
    private boolean enabled; // Para saber si está activo o borrado lógico
}