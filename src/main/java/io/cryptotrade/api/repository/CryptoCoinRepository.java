package io.cryptotrade.api.repository;

import io.cryptotrade.api.model.CryptoCoin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CryptoCoinRepository extends JpaRepository<CryptoCoin, Integer> {

    List<CryptoCoin> findByCurrencies_Symbol(String symbol);
}
