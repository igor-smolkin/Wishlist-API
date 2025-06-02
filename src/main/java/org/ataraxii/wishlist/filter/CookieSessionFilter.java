package org.ataraxii.wishlist.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ataraxii.wishlist.database.entity.User;
import org.ataraxii.wishlist.service.SessionService;
import org.ataraxii.wishlist.config.SecurityProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CookieSessionFilter extends OncePerRequestFilter {

    private final SessionService sessionService;
    private final SecurityProperties securityProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if (securityProperties.getPublicPaths().stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        Cookie[] cookies = request.getCookies();
        String sessionId = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("sessionId".equals(cookie.getName())) {
                    sessionId = cookie.getValue();
                    log.info("Найден cookie sessionId: {}", sessionId);
                    break;
                }
            }
        } else {
            log.info("В запросе отсутствуют cookies");
        }

        if (sessionId == null) {
            log.warn("Отсутствует cookie с именем sessionId");
        }

        if (sessionId == null || !sessionService.isValid(sessionId)) {
            log.warn("Неавторизованный запрос: отсутствует или недействительный sessionId");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("""
                    {
                        "status": 401,
                        "error": "Unauthorized",
                        "message": "Сессия не найдена"
                    }
                    """);
            return;
        }

        User user = sessionService.getUserBySessionId(sessionId);
        log.info("Пользователь {} аутентифицирован по sessionId", user.getUsername());
        request.setAttribute("currentUser", user);
        filterChain.doFilter(request, response);
    }
}
