package com.example.sistemagestion.dto.usuario;

import lombok.Data;

@Data
public class CreateUserDto {
    private String username;
    private String apellido;
    private String email;
    private String password;
    private String role; // Esperamos "ADMIN" o "USER"
}