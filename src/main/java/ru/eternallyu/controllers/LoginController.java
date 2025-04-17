package ru.eternallyu.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.eternallyu.dto.LoginUserDto;
import ru.eternallyu.exception.UserAuthorizationException;
import ru.eternallyu.service.AuthenticationService;
import ru.eternallyu.service.SessionService;
import ru.eternallyu.service.UserService;
import ru.eternallyu.util.CookieUtil;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    private final CookieUtil cookieUtil;

    private final AuthenticationService authenticationService;

    private final SessionService sessionService;

    @GetMapping("/login")
    public String loginGet(Model model) {
        model.addAttribute("user", new LoginUserDto());
        return "sign-in";
    }

    @PostMapping("/login")
    public String loginPost(@ModelAttribute("user") LoginUserDto user, BindingResult result, Model model, HttpServletResponse response) {
        if (userService.getUserByLogin(user.getLogin()) == null) {
            result.rejectValue("login", "error.user", "User not found.");
            return "sign-in";
        }

        if (!userService.correctPassword(user)) {
            result.rejectValue("password", "error.user", "Incorrect password.");
            return "sign-in";
        }

        UUID session = authenticationService.login(user);

        Cookie cookie = cookieUtil.setCookie(session);

        response.addCookie(cookie);

        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout(@CookieValue("session") String session, HttpServletResponse response) {

        sessionService.deleteSessionByCookieValue(session);

        Cookie cookie = cookieUtil.emptyCookie(session);
        response.addCookie(cookie);

        return "redirect:/home";
    }

}
