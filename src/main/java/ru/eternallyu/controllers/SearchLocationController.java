package ru.eternallyu.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.eternallyu.dto.LocationDto;
import ru.eternallyu.dto.SearchLocationDto;
import ru.eternallyu.dto.UserDto;
import ru.eternallyu.exception.InvalidLocationException;
import ru.eternallyu.exception.UserAuthorizationException;
import ru.eternallyu.exception.WeatherApiException;
import ru.eternallyu.model.entity.Session;
import ru.eternallyu.service.LocationService;
import ru.eternallyu.service.SessionService;
import ru.eternallyu.service.UserService;
import ru.eternallyu.util.LocationNameValidator;
import ru.eternallyu.util.SessionUtil;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchLocationController {

    private final LocationService locationService;

    private final UserService userService;

    private final SessionService sessionService;

    @GetMapping("/search")
    public String searchLocation(@CookieValue(value = "session", defaultValue = "") String sessionFromCookie, @RequestParam("name") String name, Model model) {

        Session session = sessionService.checkUserSessionStatus(sessionFromCookie);
        UserDto userDto = userService.getUserDto(session.getUser().getLogin());
        LocationNameValidator.validateLocationName(name);
        List<SearchLocationDto> locations = locationService.getLocationsByName(name);

        model.addAttribute("user", userDto);
        model.addAttribute("locations", locations);

        return "search-results";
    }

    @PostMapping("/add")
    public String addLocation(@RequestParam("latitude") BigDecimal latitude,
                              @RequestParam("longitude") BigDecimal longitude,
                              @RequestParam("name") String name,
                              @CookieValue(value = "session", defaultValue = "") String sessionFromCookie) {

        Session session = sessionService.checkUserSessionStatus(sessionFromCookie);

        int userId = session.getUser().getId();

        if (locationService.userHasLocation(userId, name, latitude, longitude)) {
            throw new InvalidLocationException("User already has this location");
        }

        LocationDto locationDto = buildLocationDto(latitude, longitude, name, userId);

        locationService.createLocation(locationDto);

        return "redirect:/home";
    }

    private static LocationDto buildLocationDto(BigDecimal latitude, BigDecimal lon, String name, int userId) {
        return LocationDto.builder()
                .latitude(latitude)
                .longitude(lon)
                .name(name)
                .userId(userId)
                .build();
    }

    @ExceptionHandler(InvalidLocationException.class)
    public String handleInvalidLocationException(InvalidLocationException exception, Model model) {
        model.addAttribute("error", exception.getMessage());
        return "error";
    }

    @ExceptionHandler(WeatherApiException.class)
    public String handleWeatherApiException(WeatherApiException exception, Model model) {
        model.addAttribute("error", exception.getMessage());
        return "error";
    }

    @ExceptionHandler(UserAuthorizationException.class)
    public String handleUserAuthorizationException(UserAuthorizationException exception, Model model) {
        model.addAttribute("error", exception.getMessage());
        return "error";
    }
}
