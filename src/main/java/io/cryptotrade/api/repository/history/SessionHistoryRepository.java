package io.cryptotrade.api.repository.history;

import io.cryptotrade.api.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SessionHistoryRepository extends JpaRepository<Session, UUID> {

    List<Session> findByUserIdAndIsActive(Integer userId, boolean isActive);

    List<Session> findByIsActiveFalse();

}