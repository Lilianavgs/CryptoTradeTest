package io.cryptotrade.api.service;

import io.cryptotrade.api.model.Session;
import io.cryptotrade.api.repository.transactional.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private static final Logger sessionLogger =
            LoggerFactory.getLogger("io.cryptotrade.api.auth");

    public Session createSession(Integer userId, String ipAddress) {
        // Validar que ipAddress no sea nulo ni vacío, si quieres puedes hacer alguna validación extra aquí
        if (ipAddress != null && ipAddress.isBlank()) {
            ipAddress = null; // Convertir cadenas vacías a null para evitar almacenar valores inválidos
        }

        Instant now = Instant.now();

        Session session = Session.builder()
                .userId(userId)
                .ipAddress(ipAddress)  // Ahora ipAddress es String directamente
                .sessionStart(now)
                .lastActivity(now)
                .isActive(true)
                .build();

        try {
            Session savedSession = sessionRepository.save(session);
            sessionLogger.info("Sesión creada para usuario {} con ID de sesión {}", userId, savedSession.getId());
            return savedSession;
        } catch (Exception e) {
            sessionLogger.error("Error al crear sesión", e);
        }
        return null;
    }

    public void updateLastActivity(UUID sessionId) {
        try {
            sessionRepository.findById(sessionId).ifPresentOrElse(session -> {
                session.setLastActivity(Instant.now());
                sessionRepository.save(session);
                sessionLogger.info("Última actividad actualizada para sesión con ID {}", sessionId);
            }, () -> {
                sessionLogger.warn("No se encontró sesión con ID {}", sessionId);
            });
        } catch (Exception e) {
            sessionLogger.error("Error al actualizar última actividad para sesión con ID {}: {}", sessionId, e.getMessage(), e);
        }
    }

    public void deactivateSessionById(String sessionIdStr) {
        try {
            UUID sessionId = UUID.fromString(sessionIdStr);
            Optional<Session> sessionOpt = sessionRepository.findById(sessionId);
            sessionOpt.ifPresent(session -> {
                session.setActive(false);
                sessionRepository.save(session);
            });
            sessionLogger.info("Se ha cerrado la sesión");
        } catch (Exception e) {
            sessionLogger.error("Error al cerrar sesión");
        }
    }

    public List<Session> getActiveSessionsByUser(Integer userId) {
        try {
            return sessionRepository.findByUserIdAndIsActive(userId, true);
        } catch (Exception e) {
            sessionLogger.error("Error al obtener sesiones activas para el usuario", e);
            // Retornar lista vacía para evitar null y facilitar manejo en capas superiores
            return Collections.emptyList();
        }
    }
}