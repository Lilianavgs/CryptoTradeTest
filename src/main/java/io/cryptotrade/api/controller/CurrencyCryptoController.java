package io.cryptotrade.api.controller;

import io.cryptotrade.api.model.Currency;
import io.cryptotrade.api.model.CryptoCoin;
import io.cryptotrade.api.service.CurrencyService;
import io.cryptotrade.api.service.CryptoCoinService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/moneda")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Currency>> getAllCurrencies() {
        return ResponseEntity.ok(currencyService.findAll());
    }

    @PostMapping("/moneda/crear_nueva_moneda")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createCurrency(@Valid @RequestBody Currency currency) {
        Currency currencyExists = currencyService.findBySymbolOrName(currency.getSymbol(), currency.getName());
        if (currencyExists != null) {
            // La moneda ya existe, devolver 409 Conflict con mensaje
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("La moneda con ese símbolo o nombre ya existe.");
        } else {
            Currency createdCurrency = currencyService.createCurrency(currency);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCurrency);
        }
    }


    @GetMapping("/criptomoneda")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CryptoCoin>> getCryptoCoins(@RequestParam(required = false) String moneda) {
        if (moneda != null && !moneda.isBlank()) {

            return ResponseEntity.ok(cryptoCoinService.findByCurrencySymbol(moneda));
        }
        return ResponseEntity.ok(cryptoCoinService.findAll());
    }

    @PostMapping("/criptomonedas/crear_criptomoneda")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createCryptoCoin(@Valid @RequestBody CryptoCoinRequest coin) {
        CryptoCoin cryptoCoin = CryptoCoin.builder()
                .name(coin.getName())
                .symbol(coin.getSymbol())
                .description(coin.getDescription())
                .build();

        CryptoCoin created = cryptoCoinService.createCryptoCoin(cryptoCoin, coin.getCurrencySymbols());
        return ResponseEntity.ok(created);
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