package io.cryptotrade.api.scheduler;

import io.cryptotrade.api.service.ReplicacionUserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulerReplicacionUsuarios {

    private final ReplicacionUserService replicacionUserService;

    public SchedulerReplicacionUsuarios(ReplicacionUserService replicacionUserService) {
        this.replicacionUserService = replicacionUserService;
    }

    @Scheduled(cron = "0 50 20 * * ?")
    public void ejecutarReplicacion() {
        replicacionUserService.replicarUsuarios();
    }
}

