package com.example.sistemagestion.repository;

import com.example.sistemagestion.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    // Buscar por refresh token
    Optional<Session> findByRefreshToken(String refreshToken);

}