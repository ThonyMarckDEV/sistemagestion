package com.example.sistemagestion.repository;

import com.example.sistemagestion.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    // Método para buscar un rol por su nombre (ej: "USER")
    Optional<Role> findByNombre(String nombre);
}