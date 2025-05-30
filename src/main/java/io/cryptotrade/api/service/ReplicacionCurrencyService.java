package io.cryptotrade.api.service;

import io.cryptotrade.api.repository.history.CurrencyHistoryRepository;
import io.cryptotrade.api.model.Currency;
import io.cryptotrade.api.repository.transactional.CurrencyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReplicacionCurrencyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReplicacionCurrencyService.class);

    private final CurrencyRepository transactionalRepo;
    private final CurrencyHistoryRepository historyRepo;

    public ReplicacionCurrencyService(CurrencyRepository transactionalRepo,
                                      CurrencyHistoryRepository historicaRepo) {
        this.transactionalRepo = transactionalRepo;
        this.historyRepo = historicaRepo;
    }

    @Transactional
    public void replicarCryptoCoins(LocalDateTime fechaCorte) {
        try {
            List<Currency> registros = transactionalRepo.findByCreatedAtBefore(fechaCorte);
            LOGGER.info("Inicio de replicación de monedas anteriores a {}", fechaCorte);
            LOGGER.info("Se encontraron {} registros para replicar", registros.size());

            for (Currency registro : registros) {
                Currency historico = new Currency();
                historico.setSymbol(registro.getSymbol());
                historico.setName(registro.getName());
                historico.setCreatedAt(registro.getCreatedAt());

                historyRepo.save(historico);
                transactionalRepo.delete(registro);

                LOGGER.info("Moneda replicada correctamente: symbol={}", registro.getSymbol());
            }

            LOGGER.info("Finalización exitosa de replicación de monedas");
        } catch (Exception e) {
            LOGGER.error("Error al replicar monedas", e);
        }
    }
}