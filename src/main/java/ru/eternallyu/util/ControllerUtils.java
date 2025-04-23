package ru.eternallyu.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import ru.eternallyu.dto.UserDto;
import ru.eternallyu.dto.weather.WeatherDto;
import ru.eternallyu.model.entity.Session;
import ru.eternallyu.service.LocationService;
import ru.eternallyu.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ControllerUtils {

    private final UserService userService;

    private final LocationService locationService;

    public void addNonEmptyAttributes(Model model, Session session) {
        UserDto userDto = userService.getUserDto(session.getUser().getLogin());
        Long userId = userService.getUserByLogin(userDto.getLogin()).getId();
        List<WeatherDto> weatherDtoList = locationService.getWeatherForUserLocationsByUserId(userId);
        model.addAttribute("user", userDto);
        model.addAttribute("weatherDtoList", weatherDtoList);
    }

    public void addEmptyAttributes(Model model) {
        model.addAttribute("user", null);
        model.addAttribute("weatherDtoList", new ArrayList<>());
    }
}
