package io.cryptotrade.api.service;

import io.cryptotrade.api.model.Session;
import io.cryptotrade.api.repository.SessionRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {

    private static final Logger authLogger = LoggerFactory.getLogger("io.cryptotrade.api.auth");

    private final SessionRepository sessionRepository;

    public Session createSession(Integer userId, String ipAddress) {
        Session session = Session.builder()
                .userId(userId)
                .ipAddress(ipAddress)
                .sessionStart(Instant.now())
                .lastActivity(Instant.now())
                .isActive(true)
                .build();
        Session savedSession = sessionRepository.save(session);
        authLogger.info("Nueva sesión creada: userId={}, sessionId={}, ipAddress={}", userId, savedSession.getId(), ipAddress);
        return savedSession;
    }

    public void updateLastActivity(UUID sessionId) {
        sessionRepository.findById(sessionId).ifPresent(session -> {
            session.setLastActivity(Instant.now());
            sessionRepository.save(session);
            authLogger.info("Actualizada última actividad: sessionId={}", sessionId);
        });
    }

    public void deactivateSessionById(String sessionIdStr) {
        try {
            UUID sessionId = UUID.fromString(sessionIdStr);
            Optional<Session> sessionOpt = sessionRepository.findById(sessionId);
            if (sessionOpt.isPresent()) {
                Session session = sessionOpt.get();
                session.setActive(false);
                sessionRepository.save(session);
                authLogger.info("Sesión desactivada: sessionId={}", sessionId);
            } else {
                authLogger.warn("Intento de desactivar sesión no encontrada: sessionId={}", sessionId);
            }
        } catch (IllegalArgumentException e) {
            authLogger.error("Formato inválido de sessionId para desactivar sesión: {}", sessionIdStr, e);
        }
    }

    // Opcional: obtener sesiones activas de un usuario
    public List<Session> getActiveSessionsByUser(Integer userId) {
        List<Session> sessions = sessionRepository.findByUserIdAndIsActive(userId, true);
        authLogger.info("Sesiones activas obtenidas para userId={}, count={}", userId, sessions.size());
        return sessions;
    }
}
