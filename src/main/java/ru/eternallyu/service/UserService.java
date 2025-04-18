package ru.eternallyu.service;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import ru.eternallyu.dto.LoginUserDto;
import ru.eternallyu.dto.RegistrationUserDto;
import ru.eternallyu.dto.UserDto;
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
        return userRepository.findByLogin(login).orElse(null);
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public void checkUniqueUserLogin(String login) {
        if (userRepository.existsByLogin(login)) {
            throw new UserAuthorizationException("User already exists");
        }
    }

    public boolean correctPassword(LoginUserDto user) {
        String rawPassword = user.getPassword();

        String storedHash = Objects.requireNonNull(userRepository.findByLogin(user.getLogin()).orElse(null)).getPassword();

        return BCrypt.checkpw(rawPassword, storedHash);
    }
}
