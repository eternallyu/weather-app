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
import ru.eternallyu.model.entity.Session;
import ru.eternallyu.service.LocationService;
import ru.eternallyu.service.SessionService;
import ru.eternallyu.util.ControllerUtils;
import ru.eternallyu.util.OpenWeatherApiClient;
import ru.eternallyu.util.SessionUtil;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class HomePageController {

    private final SessionService sessionService;

    private final ControllerUtils controllerUtils;

    private final LocationService locationService;

    private final SessionUtil sessionUtil;

    @GetMapping("/home")
    public String homePage(@CookieValue(value = "session", defaultValue = "") String sessionFromCookie, Model model) {

        if (sessionFromCookie.isEmpty()) {
            controllerUtils.addEmptyAttributes(model);
            return "index";
        }

        Session session = sessionService.getSession(UUID.fromString(sessionFromCookie));

        sessionUtil.isInvalidSession(session);

        controllerUtils.addNonEmptyAttributes(model, session);
        return "index";
    }


    @PostMapping("/delete")
    public String deleteLocation(@CookieValue(value = "session", defaultValue = "") String sessionFromCookie,
                                 @RequestParam("locationId") Long locationId) {

        Session session = sessionService.getSession(UUID.fromString(sessionFromCookie));

        sessionUtil.isInvalidSession(session);

        Long userId = session.getUser().getId();
        locationService.deleteLocationById(locationId, userId);
        return "redirect:/home";
    }
}
