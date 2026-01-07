package com.example.sistemagestion.repository;

import com.example.sistemagestion.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmail(String email);

    // Para validar si ya existe al crear uno nuevo
    boolean existsByEmail(String email);

    // Opcional: Si quieres buscar solo los activos
    List<Usuario> findByEnabledTrue();
}