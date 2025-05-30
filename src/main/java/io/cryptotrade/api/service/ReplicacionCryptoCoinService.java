package io.cryptotrade.api.service;

import io.cryptotrade.api.repository.history.CryptoCoinHistoryRepository;
import io.cryptotrade.api.model.CryptoCoin;
import io.cryptotrade.api.repository.transactional.CryptoCoinRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReplicacionCryptoCoinService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReplicacionCryptoCoinService.class);
    private final CryptoCoinRepository transaccionalRepo;
    private final CryptoCoinHistoryRepository historicaRepo;

    public ReplicacionCryptoCoinService(CryptoCoinRepository transaccionalRepo,
                                        CryptoCoinHistoryRepository historicaRepo) {
        this.transaccionalRepo = transaccionalRepo;
        this.historicaRepo = historicaRepo;
    }

    @Transactional
    public void replicarCryptoCoins(LocalDateTime fechaCorte) {
        LOGGER.info("Inicio de replicación de CryptoCoins anteriores a {}", fechaCorte);

        try {
            List<CryptoCoin> registros = transaccionalRepo.findByCreatedAtBefore(fechaCorte);
            LOGGER.info("Se encontraron {} registros para replicar", registros.size());

            for (CryptoCoin registro : registros) {
                LOGGER.debug("Replicando CryptoCoin con ID: {}", registro.getId());

                CryptoCoin historico = new CryptoCoin();
                historico.setSymbol(registro.getSymbol());
                historico.setName(registro.getName());
                historico.setDescription(registro.getDescription());
                historico.setCreatedAt(registro.getCreatedAt());

                historicaRepo.save(historico);
                LOGGER.debug("CryptoCoin histórica guardada para ID: {}", registro.getId());

                transaccionalRepo.delete(registro);
                LOGGER.debug("CryptoCoin original eliminada con ID: {}", registro.getId());

                LOGGER.info("CryptoCoin replicada correctamente: id={}", registro.getId());
            }

            LOGGER.info("Finalización exitosa de replicación de CryptoCoins");
        } catch (Exception e) {
            LOGGER.error("Error al replicar CryptoCoins", e);
        }
    }
}