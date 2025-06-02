package org.ataraxii.wishlist.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CookieUtil {

    private static final String NAME = "sessionId";

    public static void addSessionCookie(HttpServletResponse response, UUID sessionId) {
        try {
            Cookie cookie = new Cookie(NAME, sessionId.toString());
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(30 * 60);
            response.addCookie(cookie);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteSessionCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(NAME, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
