package io.cryptotrade.api.service;

import io.cryptotrade.api.repository.history.UserHistoryRepository;
import io.cryptotrade.api.model.User;
import io.cryptotrade.api.repository.transactional.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplicacionUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReplicacionUserService.class);

    private final UserRepository userTransaccionalRepo;
    private final UserHistoryRepository userHistoricoRepo;

    public ReplicacionUserService(UserRepository userTransaccionalRepo,
                                  UserHistoryRepository userHistoricoRepo) {
        this.userTransaccionalRepo = userTransaccionalRepo;
        this.userHistoricoRepo = userHistoricoRepo;
    }

    @Transactional
    public void replicarUsuarios() {
        try {
            List<User> usuarios = userTransaccionalRepo.findAll();

            for (User u : usuarios) {
                userHistoricoRepo.save(u);
                LOGGER.info("Usuario replicado correctamente: userId={}", u.getId());
            }
        } catch (Exception e) {
            LOGGER.error("Error al replicar usuarios", e);
        }
    }
}
