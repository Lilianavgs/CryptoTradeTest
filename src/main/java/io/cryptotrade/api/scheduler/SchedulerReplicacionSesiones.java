package io.cryptotrade.api.scheduler;

import io.cryptotrade.api.service.ReplicacionSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulerReplicacionSesiones {

    private final ReplicacionSessionService replicacionSessionService;
    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerReplicacionSesiones.class);


    public SchedulerReplicacionSesiones(ReplicacionSessionService replicacionSessionService) {
        this.replicacionSessionService = replicacionSessionService;
    }

    @Scheduled(cron = "0 05 24 * * ?")
    public void ejecutarReplicacion() {
        LOGGER.info("Se dispara la tarea");
        replicacionSessionService.replicarSesiones();
    }
}

