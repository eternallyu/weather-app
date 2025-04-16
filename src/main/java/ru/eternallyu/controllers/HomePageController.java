package ru.eternallyu.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.eternallyu.dto.UserDto;
import ru.eternallyu.dto.weather.WeatherDto;
import ru.eternallyu.model.entity.Session;
import ru.eternallyu.service.LocationService;
import ru.eternallyu.service.SessionService;
import ru.eternallyu.service.UserService;
import ru.eternallyu.util.SessionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class HomePageController {

    private final SessionService sessionService;

    private final UserService userService;

    private final LocationService locationService;

    private final SessionUtil sessionUtil;

    @GetMapping("/home")
    public String homePage(@CookieValue(value = "session", defaultValue = "") String sessionFromCookie, Model model) {

        if (sessionFromCookie.isEmpty()) {
            addEmptyAttributes(model);
            return "index";
        }

        Session session = sessionService.getSession(UUID.fromString(sessionFromCookie));

        if (sessionUtil.isInvalidSession(session)) {
            addEmptyAttributes(model);
            return "index";
        }

        addNonEmptyAttributes(model, session);
        return "index";
    }


    @PostMapping("/delete")
    public String deleteLocation(@CookieValue(value = "session", defaultValue = "") String sessionFromCookie,
                                 @RequestParam("locationId") Long locationId) {
        Session session = sessionService.getSession(UUID.fromString(sessionFromCookie));
        if (sessionUtil.isInvalidSession(session)) {
            return "redirect:/login";
        }
        Integer userId = session.getUser().getId();
        locationService.deleteLocationById(locationId, userId);
        return "redirect:/home";
    }



    private void addNonEmptyAttributes(Model model, Session session) {
        UserDto userDto = userService.getUserDto(session.getUser().getLogin());
        int userId = userService.getUserByLogin(userDto.getLogin()).getId();
        List<WeatherDto> weatherDtoList = locationService.getWeatherForUserLocationsByUserId(userId);
        model.addAttribute("user", userDto);
        model.addAttribute("weatherDtoList", weatherDtoList);
    }

    private static void addEmptyAttributes(Model model) {
        model.addAttribute("user", null);
        model.addAttribute("weatherDtoList", new ArrayList<>());
    }
}
