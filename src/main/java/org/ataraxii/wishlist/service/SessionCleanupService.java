package org.ataraxii.wishlist.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ataraxii.wishlist.database.repository.SessionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SessionCleanupService {

    private final SessionRepository sessionRepository;

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void cleanupExpiredSessions() {
        Instant now = Instant.now();
        sessionRepository.deleteByExpiredAtBefore(now);
    }
}
