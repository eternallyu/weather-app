package ru.eternallyu.service;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.eternallyu.dto.LoginUserDto;
import ru.eternallyu.dto.RegistrationUserDto;
import ru.eternallyu.dto.UserDto;
import ru.eternallyu.exception.NotFoundException;
import ru.eternallyu.exception.UserAuthorizationException;
import ru.eternallyu.mapper.UserMapper;
import ru.eternallyu.model.entity.User;
import ru.eternallyu.repository.UserRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserDto getUserDto(String login) {
        return userRepository.findByLogin(login).map(userMapper::mapUserToUserDto).orElse(null);
    }

    public void createUser(RegistrationUserDto registrationUserDto) {

        checkUniqueUserLogin(registrationUserDto.getLogin());

        User user = userMapper.mapUserDtoToUser(registrationUserDto);

        String salt = BCrypt.gensalt(10);
        String hashedPassword = BCrypt.hashpw(user.getPassword(), salt);
        user.setPassword(hashedPassword);

        userRepository.save(user);
    }

    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> new NotFoundException("User not found."));
    }

    public User getUserById(Long id) {
        return userRepository.findById(Math.toIntExact(id)).orElse(null);
    }

    public void checkUniqueUserLogin(String login) {
        if (userRepository.existsByLogin(login)) {
            logger.error("User with login {} already exists", login);
            throw new UserAuthorizationException("User already exists");
        }
    }

    public boolean correctPassword(LoginUserDto user) {
        String rawPassword = user.getPassword();

        String storedHash = Objects.requireNonNull(userRepository.findByLogin(user.getLogin()).orElse(null)).getPassword();

        return BCrypt.checkpw(rawPassword, storedHash);
    }
}
