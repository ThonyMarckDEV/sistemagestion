package com.example.sistemagestion.dto.role;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleResponseDto {
    private Integer id;
    private String nombre;
}