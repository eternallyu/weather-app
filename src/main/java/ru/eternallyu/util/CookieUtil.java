package ru.eternallyu.util;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static ru.eternallyu.util.SessionUtil.SESSION_DURATION_SECONDS;

@Component
public class CookieUtil {
    public Cookie setCookie(UUID session) {
        Cookie cookie = new Cookie("session", session.toString());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(SESSION_DURATION_SECONDS);
        return cookie;
    }

    public Cookie emptyCookie(String session) {
        Cookie cookie = new Cookie("session", "");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        return cookie;
    }
}
