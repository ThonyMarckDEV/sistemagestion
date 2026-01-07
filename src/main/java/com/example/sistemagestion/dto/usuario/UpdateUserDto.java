package com.example.sistemagestion.dto.usuario;

import lombok.Data;

@Data
public class UpdateUserDto {
    private String username;
    private String apellido;
    private String email;
    private String role; // Permite cambiar el rol
}