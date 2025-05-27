package org.ataraxii.wishlist.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ataraxii.wishlist.database.entity.Session;
import org.ataraxii.wishlist.database.entity.User;
import org.ataraxii.wishlist.database.repository.SessionRepository;
import org.ataraxii.wishlist.database.repository.UserRepository;
import org.ataraxii.wishlist.exception.NotFoundException;
import org.ataraxii.wishlist.exception.UnauthorizedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    @Transactional
    public void deleteBySessionId(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            return;
        }

        UUID uuid;
        try {
            uuid = UUID.fromString(sessionId);
        } catch (Exception e) {
            return;
        }

        sessionRepository.findBySessionId(uuid).ifPresent(session -> {
            sessionRepository.deleteBySessionId(uuid);
        });
    }

    public boolean isValid(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            return false;
        }
        try {
            UUID uuid = UUID.fromString(sessionId);
            return sessionRepository.findBySessionId(uuid).isPresent();
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public User getUserBySessionId(String sessionId) {
        UUID uuid;

        try {
            uuid = UUID.fromString(sessionId);
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException("Неверный формат sessionId");
        }

        return sessionRepository.findBySessionId(uuid)
                .map(Session::getUser)
                .orElseThrow(() -> new UnauthorizedException("Сессия не найдена"));
    }
}
