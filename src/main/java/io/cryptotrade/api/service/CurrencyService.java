package io.cryptotrade.api.service;

import io.cryptotrade.api.model.Currency;
import io.cryptotrade.api.repository.transactional.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private static final Logger transactionLogger = LoggerFactory.getLogger("io.cryptotrade.api.transaction");

    private final CurrencyRepository currencyRepository;

    public List<Currency> findAll() {
        List<Currency> currencies = currencyRepository.findAll();
        transactionLogger.info("Obtenidas {} monedas desde la base de datos", currencies.size());
        return currencies;
    }

    public Currency createCurrency(Currency currency) {

        Currency savedCurrency = currencyRepository.save(currency);
        transactionLogger.info("Nueva moneda creada: id={}, symbol={}, name={}",
                savedCurrency.getId(), savedCurrency.getSymbol(), savedCurrency.getName());
        return savedCurrency;
    }

    public Currency findBySymbol(String symbol) {
        Currency currency = currencyRepository.findBySymbol(symbol).orElse(null);
        if (currency != null) {
            transactionLogger.info("Moneda encontrada por símbolo '{}': id={}", symbol, currency.getId());
        } else {
            transactionLogger.warn("No se encontró moneda con símbolo '{}'", symbol);
        }
        return currency;
    }

    public Currency findBySymbolOrName(String symbol, String name) {
        Currency currency = currencyRepository.findBySymbolOrName(symbol, name).orElse(null);
        if (currency != null) {
            transactionLogger.info("Moneda encontrada por símbolo '{}' o nombre '{}': id={}", symbol,name, currency.getId());
        } else {
            transactionLogger.warn("No se encontró moneda con símbolo '{}' o nombre '{}'", symbol,name);
        }
        return currency;
    }


}