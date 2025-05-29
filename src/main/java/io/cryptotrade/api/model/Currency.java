package io.cryptotrade.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "currency", schema = "currencies")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 10, nullable = false, unique = true)
    private String symbol;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100)
    private String country;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Relación bidireccional con CryptoCoin
    @ManyToMany(mappedBy = "currencies")
    private Set<CryptoCoin> cryptoCoins;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
