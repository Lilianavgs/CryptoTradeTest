package io.cryptotrade.api.scheduler;

import io.cryptotrade.api.service.ReplicacionCryptoCoinService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SchedulerReplicacionCryptoCoins {

    private final ReplicacionCryptoCoinService replicacionService;

    public SchedulerReplicacionCryptoCoins(ReplicacionCryptoCoinService replicacionService) {
        this.replicacionService = replicacionService;
    }

    @Scheduled(cron = "0 50 22 * * ?")
    public void ejecutarReplicacion() {
        LocalDateTime fechaCorte = LocalDateTime.now().minusDays(30);
        replicacionService.replicarCryptoCoins(fechaCorte);
    }
}

