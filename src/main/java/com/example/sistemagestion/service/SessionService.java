package com.example.sistemagestion.service;

import com.example.sistemagestion.model.Session;
import com.example.sistemagestion.model.Usuario;
import com.example.sistemagestion.repository.SessionRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId; // Importar ZoneId

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    public void createSession(Usuario usuario, String accessToken, String refreshToken, HttpServletRequest request) {
        Session session = new Session();
        session.setUsuario(usuario);
        session.setAccessToken(accessToken);
        session.setRefreshToken(refreshToken);

        // 1. Obtener la hora actual en Perú
        ZoneId zonePeru = ZoneId.of("America/Lima");
        LocalDateTime ahoraEnPeru = LocalDateTime.now(zonePeru);

        // 2. CONFIGURAR EXPIRACIÓN SEGÚN TUS REGLAS:
        // Access Token: Vence en 5 minutos
        session.setAccessExpiresAt(ahoraEnPeru.plusMinutes(5));

        // Refresh Token: Vence en 24 horas (1 día)
        session.setRefreshExpiresAt(ahoraEnPeru.plusHours(24));

        // Metadatos
        session.setIpAddress(getClientIp(request));
        session.setDevice(request.getHeader("User-Agent"));

        sessionRepository.save(session);
    }

    private String getClientIp(HttpServletRequest request) {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr;
    }
}