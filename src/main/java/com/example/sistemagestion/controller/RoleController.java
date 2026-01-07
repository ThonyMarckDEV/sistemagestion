package com.example.sistemagestion.controller;

import com.example.sistemagestion.dto.role.RoleResponseDto;
import com.example.sistemagestion.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    
    @GetMapping("/index")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<RoleResponseDto>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        int currentPage = (page > 0) ? page - 1 : 0;

        Pageable pageable = PageRequest.of(currentPage, size);

        return ResponseEntity.ok(roleService.getAllRoles(pageable));
    }
}