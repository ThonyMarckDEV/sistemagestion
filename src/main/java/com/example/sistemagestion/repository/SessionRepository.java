package com.example.sistemagestion.repository;

import com.example.sistemagestion.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    // Puedes agregar métodos extra si necesitas, por ejemplo buscar por token
    Optional<Session> findByAccessToken(String accessToken);
}