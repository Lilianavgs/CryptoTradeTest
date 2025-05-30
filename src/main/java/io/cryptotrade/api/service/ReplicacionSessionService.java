package io.cryptotrade.api.service;

import io.cryptotrade.api.repository.history.SessionHistoryRepository;
import io.cryptotrade.api.model.Session;
import io.cryptotrade.api.repository.transactional.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReplicacionSessionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReplicacionSessionService.class);

    private final SessionRepository transaccionalRepo;
    private final SessionHistoryRepository historicoRepo;

    public ReplicacionSessionService(SessionRepository transaccionalRepo,
                                     SessionHistoryRepository historicoRepo) {
        this.transaccionalRepo = transaccionalRepo;
        this.historicoRepo = historicoRepo;
    }

    @Transactional
    public void replicarSesiones() {
        try {
            // Ejemplo: replicar sesiones inactivas
            List<Session> sesiones = transaccionalRepo.findByIsActiveFalse();

            for (Session sesion : sesiones) {
                Session historico = new Session();
                historico.setId(sesion.getId()); // conserva el mismo UUID
                historico.setUserId(sesion.getUserId());
                historico.setSessionStart(sesion.getSessionStart());
                historico.setLastActivity(sesion.getLastActivity());
                historico.setActive(false);
                historico.setIpAddress(sesion.getIpAddress());

                historicoRepo.save(historico);
                transaccionalRepo.delete(sesion);

                LOGGER.info("Sesión replicada correctamente: sessionId={}", sesion.getId());
            }
        } catch (Exception e) {
            LOGGER.error("Error al replicar sesiones", e);
        }
    }
}
