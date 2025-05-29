package io.cryptotrade.api.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.net.InetAddress;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "sessions", schema = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Session {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "session_start", nullable = false)
    private Instant sessionStart;

    @Column(name = "last_activity", nullable = false)
    private Instant lastActivity;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "ip_address")
    private String ipAddress;

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        if (sessionStart == null) sessionStart = now;
        if (lastActivity == null) lastActivity = now;
        isActive = true;
    }
}
