package ru.eternallyu.mapper;

import org.springframework.stereotype.Component;
import ru.eternallyu.dto.RegistrationUserDto;
import ru.eternallyu.dto.UserDto;
import ru.eternallyu.model.entity.User;

@Component
public class UserMapper {
    public UserDto mapUserToUserDto(User user) {

        if (user == null) {
            return null;
        }

        return buildUserDto(user);
    }

    private static UserDto buildUserDto(User user) {
        return UserDto.builder()
                .login(user.getLogin())
                .build();
    }

    public User mapUserDtoToUser(RegistrationUserDto registrationUserDto) {
        return new User(registrationUserDto.getLogin(), registrationUserDto.getPassword());
    }
}
