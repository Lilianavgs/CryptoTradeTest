package io.cryptotrade.api.repository.transactional;

import io.cryptotrade.api.model.CryptoCoin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CryptoCoinRepository extends JpaRepository<CryptoCoin, Integer> {
    List<CryptoCoin> findByCurrencies_Symbol(String symbol);

    Optional<CryptoCoin> findBySymbol(String symbol);

    Optional<CryptoCoin> findByName(String name);

    Optional<CryptoCoin> findBySymbolOrName(String symbol, String name);

    List<CryptoCoin> findByCreatedAtBefore(LocalDateTime fecha);
}
