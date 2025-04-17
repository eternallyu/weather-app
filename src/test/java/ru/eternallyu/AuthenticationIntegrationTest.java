package ru.eternallyu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.eternallyu.dto.LoginUserDto;
import ru.eternallyu.dto.RegistrationUserDto;
import ru.eternallyu.exception.UserAuthorizationException;
import ru.eternallyu.model.entity.User;
import ru.eternallyu.repository.SessionRepository;
import ru.eternallyu.repository.UserRepository;
import ru.eternallyu.service.AuthenticationService;
import ru.eternallyu.service.UserService;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        ru.eternallyu.configuration.SpringConfig.class,
        ru.eternallyu.configuration.JpaConfig.class,
        ru.eternallyu.configuration.TestDataSourceConfig.class
})
@Transactional
@ActiveProfiles("test")
public class AuthenticationIntegrationTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    private final String LOGIN_AND_PASSWORD_KEY_WORD = "admin";

    @Test
    public void shouldRegisterNewUser() {
        RegistrationUserDto registrationUserDto = new RegistrationUserDto(LOGIN_AND_PASSWORD_KEY_WORD,
                LOGIN_AND_PASSWORD_KEY_WORD,
                LOGIN_AND_PASSWORD_KEY_WORD);

        userService.createUser(registrationUserDto);

        assertThat(userRepository.findByLogin(registrationUserDto.getLogin()).isPresent()).isTrue();

    }

    @Test
    public void registerUserWithNotUniqueLogin() {
        RegistrationUserDto registrationUserDto = new RegistrationUserDto(LOGIN_AND_PASSWORD_KEY_WORD,
                LOGIN_AND_PASSWORD_KEY_WORD,
                LOGIN_AND_PASSWORD_KEY_WORD);

        userService.createUser(registrationUserDto);

        RegistrationUserDto notUniqueUser = new RegistrationUserDto(LOGIN_AND_PASSWORD_KEY_WORD,
                LOGIN_AND_PASSWORD_KEY_WORD,
                LOGIN_AND_PASSWORD_KEY_WORD);

        Assertions.assertThrows(UserAuthorizationException.class, () -> userService.createUser(notUniqueUser));
    }

    @Test
    public void createSessionWhenUserLogin() {
        User user = new User();
        user.setLogin(LOGIN_AND_PASSWORD_KEY_WORD);
        user.setPassword(LOGIN_AND_PASSWORD_KEY_WORD);
        userRepository.save(user);

        LoginUserDto loginUserDto = new LoginUserDto(LOGIN_AND_PASSWORD_KEY_WORD, LOGIN_AND_PASSWORD_KEY_WORD);
        UUID uuidOfCreatedSession = authenticationService.login(loginUserDto);

        assertThat(sessionRepository.findById(uuidOfCreatedSession).isPresent()).isTrue();
    }
}
