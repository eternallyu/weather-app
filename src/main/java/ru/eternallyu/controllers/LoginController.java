package ru.eternallyu.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.eternallyu.dto.LoginUserDto;
import ru.eternallyu.service.AuthenticationService;
import ru.eternallyu.service.SessionService;
import ru.eternallyu.service.UserService;
import ru.eternallyu.util.CookieUtils;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    private final CookieUtils cookieUtils;

    private final AuthenticationService authenticationService;

    private final SessionService sessionService;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/login")
    public String loginGet(Model model) {
        model.addAttribute("user", new LoginUserDto());
        return "sign-in";
    }

    @PostMapping("/login")
    public String loginPost(@ModelAttribute("user") LoginUserDto user, BindingResult result, HttpServletResponse response) {
        if (userService.getUserByLogin(user.getLogin()) == null) {
            logger.warn("User {} not found", user.getLogin());
            result.rejectValue("login", "error.user", "User not found.");
            return "sign-in";
        }

        if (!userService.correctPassword(user)) {
            logger.warn("User {} incorrect password", user.getLogin());
            result.rejectValue("password", "error.user", "Incorrect password.");
            return "sign-in";
        }

        UUID session = authenticationService.login(user);

        Cookie cookie = cookieUtils.setCookie(session);

        response.addCookie(cookie);

        return "redirect:/home";
    }

    @PostMapping("/logout")
    public String logout(@CookieValue("session") String session, HttpServletResponse response) {

        sessionService.deleteSessionByCookieValue(session);

        Cookie cookie = cookieUtils.emptyCookie(session);
        response.addCookie(cookie);

        return "redirect:/home";
    }

}
