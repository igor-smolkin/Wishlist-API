package org.ataraxii.wishlist.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ataraxii.wishlist.database.entity.User;
import org.ataraxii.wishlist.database.repository.SessionRepository;
import org.ataraxii.wishlist.exception.UnauthorizedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    @Transactional
    public void deleteBySessionId(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            log.debug("Попытка удалить сессию с пустым sessionId");
            return;
        }

        UUID uuid;
        try {
            uuid = UUID.fromString(sessionId);
        } catch (Exception e) {
            log.warn("Попытка удалить сессию с некорректным UUID: {}", sessionId);
            return;
        }

        sessionRepository.findBySessionId(uuid).ifPresent(session -> {
            sessionRepository.deleteBySessionId(uuid);
            log.info("Удалена сессия {} пользователя {}", uuid, session.getUser().getUsername());
        });
    }

    public boolean isValid(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            log.debug("Проверка валидности пустого sessionId");
            return false;
        }
        try {
            UUID uuid = UUID.fromString(sessionId);
            boolean exists = sessionRepository.findBySessionId(uuid).isPresent();
            log.debug("Сессия {} валидна: {}", uuid, exists);
            return exists;
        } catch (IllegalArgumentException ex) {
            log.warn("Неверный формат sessionId при проверке валидности: {}", sessionId);
            return false;
        }
    }

    public User getUserBySessionId(String sessionId) {
        UUID uuid;

        try {
            uuid = UUID.fromString(sessionId);
        } catch (IllegalArgumentException e) {
            log.warn("Неверный формат sessionId при получении пользователя: {}", sessionId);
            throw new UnauthorizedException("Неверный формат sessionId");
        }

        return sessionRepository.findBySessionId(uuid)
                .map(session -> {
                    log.info("Получен пользователь: {} по сессии {}", session.getUser().getUsername(), uuid);
                    return session.getUser();
                })
                .orElseThrow(() -> {
                    log.warn("Сессия {} не найдена при попытке получения пользователя", uuid);
                    return new UnauthorizedException("Сессия не найдена");
                });
    }
}
