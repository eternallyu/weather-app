package ru.eternallyu.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.eternallyu.dto.LocationDto;
import ru.eternallyu.dto.SearchLocationDto;
import ru.eternallyu.dto.UserDto;
import ru.eternallyu.model.entity.Session;
import ru.eternallyu.service.LocationService;
import ru.eternallyu.service.SessionService;
import ru.eternallyu.service.UserService;
import ru.eternallyu.util.LocationNameValidator;

import java.math.BigDecimal;
import java.util.List;

import static ru.eternallyu.mapper.LocationMapper.buildLocationDto;

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

        Long userId = session.getUser().getId();
        locationService.isUserAlreadyHasLocation(userId, name, latitude, longitude);

        LocationDto locationDto = buildLocationDto(latitude, longitude, name, userId);
        locationService.createLocation(locationDto);
        return "redirect:/home";
    }
}
