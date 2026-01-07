package com.example.sistemagestion.service;

import com.example.sistemagestion.dto.role.RoleResponseDto;
import com.example.sistemagestion.model.Role;
import com.example.sistemagestion.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Page<RoleResponseDto> getAllRoles(Pageable pageable) {
        return roleRepository.findAll(pageable)
                .map(this::mapToDto);
    }

    // Método auxiliar
    private RoleResponseDto mapToDto(Role role) {
        return RoleResponseDto.builder()
                .id(role.getId())
                .nombre(role.getNombre())
                .build();
    }
}