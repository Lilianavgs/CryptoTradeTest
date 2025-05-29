package io.cryptotrade.api.controller;

import io.cryptotrade.api.model.Currency;
import io.cryptotrade.api.model.CryptoCoin;
import io.cryptotrade.api.service.CurrencyService;
import io.cryptotrade.api.service.CryptoCoinService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CurrencyCryptoController {

    private final CurrencyService currencyService;
    private final CryptoCoinService cryptoCoinService;

    // --- Monedas ---

    @GetMapping("/moneda")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Currency>> getAllCurrencies() {
        return ResponseEntity.ok(currencyService.findAll());
    }

    @PostMapping("/moneda")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Currency> createCurrency(@Valid @RequestBody Currency currency) {
        return ResponseEntity.ok(currencyService.createCurrency(currency));
    }

    // --- Criptomonedas ---

    @GetMapping("/criptomoneda")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CryptoCoin>> getCryptoCoins(@RequestParam(required = false) String moneda) {
        if (moneda != null) {
            return ResponseEntity.ok(cryptoCoinService.findByCurrencySymbol(moneda));
        }
        return ResponseEntity.ok(cryptoCoinService.findAll());
    }

    @PostMapping("/criptomonedas")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CryptoCoin> createCryptoCoin(@Valid @RequestBody CryptoCoinRequest request) {
        CryptoCoin cryptoCoin = CryptoCoin.builder()
                .name(request.getName())
                .symbol(request.getSymbol())
                .description(request.getDescription())
                .build();
        return ResponseEntity.ok(cryptoCoinService.createCryptoCoin(cryptoCoin, request.getCurrencySymbols()));
    }

    @PutMapping("/criptomonedas/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CryptoCoin> updateCryptoCoin(@PathVariable Integer id, @Valid @RequestBody CryptoCoinRequest request) {
        CryptoCoin cryptoCoin = CryptoCoin.builder()
                .name(request.getName())
                .symbol(request.getSymbol())
                .description(request.getDescription())
                .build();
        CryptoCoin updated = cryptoCoinService.updateCryptoCoin(id, cryptoCoin);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @Data
    public static class CryptoCoinRequest {
        private String symbol;
        private String name;
        private String description;
        private Set<String> currencySymbols;
    }
}
