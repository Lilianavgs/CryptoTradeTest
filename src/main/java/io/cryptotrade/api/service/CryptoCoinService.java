package io.cryptotrade.api.service;

import io.cryptotrade.api.model.CryptoCoin;
import io.cryptotrade.api.model.Currency;
import io.cryptotrade.api.repository.CryptoCoinRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CryptoCoinService {

    private static final Logger transactionLogger = LoggerFactory.getLogger("io.cryptotrade.api.transaction");

    private final CryptoCoinRepository cryptoCoinRepository;
    private final CurrencyService currencyService;

    public List<CryptoCoin> findAll() {
        List<CryptoCoin> cryptoCoins = cryptoCoinRepository.findAll();
        transactionLogger.info("Obtenidas {} criptomonedas desde la base de datos", cryptoCoins.size());
        return cryptoCoins;
    }

    public List<CryptoCoin> findByCurrencySymbol(String symbol) {
        List<CryptoCoin> cryptoCoins = cryptoCoinRepository.findByCurrencies_Symbol(symbol);
        transactionLogger.info("Obtenidas {} criptomonedas relacionadas con la moneda '{}'", cryptoCoins.size(), symbol);
        return cryptoCoins;
    }

    public CryptoCoin createCryptoCoin(CryptoCoin cryptoCoin, Set<String> currencySymbols) {
        Set<Currency> currencies = currencySymbols.stream()
                .map(currencyService::findBySymbol)
                .filter(c -> c != null)
                .collect(java.util.stream.Collectors.toSet());
        cryptoCoin.setCurrencies(currencies);
        CryptoCoin savedCryptoCoin = cryptoCoinRepository.save(cryptoCoin);
        transactionLogger.info("Nueva criptomoneda creada: id={}, symbol={}, name={}, monedas asociadas={}",
                savedCryptoCoin.getId(),
                savedCryptoCoin.getSymbol(),
                savedCryptoCoin.getName(),
                currencies.stream().map(Currency::getSymbol).toList());
        return savedCryptoCoin;
    }

    public CryptoCoin updateCryptoCoin(Integer id, CryptoCoin updatedCryptoCoin) {
        return cryptoCoinRepository.findById(id).map(cryptoCoin -> {
            cryptoCoin.setName(updatedCryptoCoin.getName());
            cryptoCoin.setSymbol(updatedCryptoCoin.getSymbol());
            cryptoCoin.setDescription(updatedCryptoCoin.getDescription());
            CryptoCoin savedCryptoCoin = cryptoCoinRepository.save(cryptoCoin);
            transactionLogger.info("Criptomoneda actualizada: id={}, symbol={}, name={}",
                    savedCryptoCoin.getId(),
                    savedCryptoCoin.getSymbol(),
                    savedCryptoCoin.getName());
            return savedCryptoCoin;
        }).orElseGet(() -> {
            transactionLogger.warn("Intento de actualizar criptomoneda no encontrada: id={}", id);
            return null;
        });
    }
}