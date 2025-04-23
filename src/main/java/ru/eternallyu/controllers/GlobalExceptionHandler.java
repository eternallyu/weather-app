package ru.eternallyu.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.eternallyu.exception.InvalidResourceException;
import ru.eternallyu.exception.NotFoundException;
import ru.eternallyu.exception.UserAuthorizationException;
import ru.eternallyu.exception.WeatherApiException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(NotFoundException exception, Model model) {
        model.addAttribute("error", exception.getMessage());
        return "error";
    }

    @ExceptionHandler(InvalidResourceException.class)
    public String handleInvalidLocationException(InvalidResourceException exception, Model model) {
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
