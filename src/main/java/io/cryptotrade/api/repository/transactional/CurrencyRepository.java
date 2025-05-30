package io.cryptotrade.api.repository.transactional;

import io.cryptotrade.api.model.CryptoCoin;
import io.cryptotrade.api.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
    Optional<Currency> findBySymbol(String symbol);

    Optional<Currency> findBySymbolOrName(String symbol, String name);

    List<Currency> findByCreatedAtBefore(LocalDateTime fecha);
}
