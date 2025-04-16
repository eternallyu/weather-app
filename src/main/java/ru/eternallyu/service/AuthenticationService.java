package ru.eternallyu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.eternallyu.dto.LoginUserDto;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final SessionService sessionService;

    private final UserService userService;

    @Transactional
    public UUID loginUser(LoginUserDto user) {

        int userId = getUserIdFromUserDto(user);

        sessionService.deleteSessionByUserId(userId);

        sessionService.createSession(userId);

        return sessionService.getSessionByUserId(userId).getId();
    }

    private int getUserIdFromUserDto(LoginUserDto user) {
        return userService.getUserByLogin(user.getLogin()).getId();
    }
}
