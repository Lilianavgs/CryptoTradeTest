package io.cryptotrade.api.scheduler;

import io.cryptotrade.api.service.ReplicacionCurrencyService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SchedulerReplicacionCurrency {

    private final ReplicacionCurrencyService replicacionService;

    public SchedulerReplicacionCurrency(ReplicacionCurrencyService replicacionService) {
        this.replicacionService = replicacionService;
    }

    @Scheduled(cron = "0 10 23 * * ?")
    public void ejecutarReplicacion() {
        LocalDateTime fechaCorte = LocalDateTime.now().minusDays(30);
        replicacionService.replicarCryptoCoins(fechaCorte);
    }
}