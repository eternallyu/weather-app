package ru.eternallyu.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.eternallyu.exception.NotFoundException;
import ru.eternallyu.model.entity.Session;
import ru.eternallyu.model.entity.User;
import ru.eternallyu.repository.SessionRepository;
import ru.eternallyu.util.SessionUtils;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    private final SessionUtils sessionUtils;

    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(SessionService.class);

    public Session getSession(UUID uuid) {
        return sessionRepository.findById(uuid).orElse(null);
    }

    public void createSession(Long userId) {
        UUID uuid = UUID.randomUUID();
        User user = userService.getUserById(userId);
        LocalDateTime sessionExpirationTime = sessionUtils.getSessionExpirationTime();

        Session session = new Session(uuid, user, sessionExpirationTime);
        sessionRepository.save(session);
    }

    public Session getSessionByUserId(Long userId) {
        return sessionRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("Session not found."));
    }

    public void deleteSessionByUserId(Long userId) {
        sessionRepository.deleteByUserId(userId);
    }

    public void deleteSessionByCookieValue(String session) {
        sessionRepository.deleteById(UUID.fromString(session));
    }

    public Session checkUserSessionStatus(String sessionFromCookie) {

        Session session = getSession(UUID.fromString(sessionFromCookie));

        sessionUtils.isInvalidSession(session);

        return session;
    }
}
