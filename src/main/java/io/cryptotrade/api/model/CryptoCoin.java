package io.cryptotrade.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "crypto_coin", schema = "currencies")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CryptoCoin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 10, nullable = false, unique = true)
    private String symbol;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Relación ManyToMany con Currency
    @ManyToMany
    @JoinTable(
            name = "crypto_coin_currency",
            schema = "currencies",
            joinColumns = @JoinColumn(name = "crypto_coin_id"),
            inverseJoinColumns = @JoinColumn(name = "currency_id")
    )
    private Set<Currency> currencies;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
