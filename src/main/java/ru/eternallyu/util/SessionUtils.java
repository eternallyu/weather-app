package ru.eternallyu.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.eternallyu.exception.InvalidResourceException;
import ru.eternallyu.model.entity.Session;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SessionUtils {

    public static final int SESSION_DURATION_SECONDS = 7200;

    public void isInvalidSession(Session session) {
        if (session == null || session.getId() == null || isExpiredSession(session)) {
            throw new InvalidResourceException("Invalid session.");
        }
    }

    private boolean isExpiredSession(Session session) {
        return LocalDateTime.now().isAfter(session.getExpiresAt());
    }

    public LocalDateTime getSessionExpirationTime() {
        return LocalDateTime.now().plusSeconds(SESSION_DURATION_SECONDS);
    }
}
