package org.ataraxii.wishlist.database.repository;

import org.ataraxii.wishlist.database.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findBySessionId(UUID sessionId);

    void deleteBySessionId(UUID sessionId);

    void deleteByExpiredAtBefore(Instant expiredAtBefore);
}
