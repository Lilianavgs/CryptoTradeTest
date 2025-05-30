package io.cryptotrade.api.repository.history;

import io.cryptotrade.api.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyHistoryRepository extends JpaRepository<Currency, Integer> {
    Optional<Currency> findBySymbol(String symbol);

    Optional<Currency> findBySymbolOrName(String symbol, String name);
}