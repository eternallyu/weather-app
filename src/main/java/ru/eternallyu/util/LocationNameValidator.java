package ru.eternallyu.util;

import org.springframework.stereotype.Component;
import ru.eternallyu.exception.InvalidLocationException;

@Component
public class LocationNameValidator {
    public static void validateLocationName(String name) {
        if (emptyName(name) || notValidLength(name) || !name.matches("[a-zA-Za-яА-Я]+")
        ) {
            throw new InvalidLocationException("Location name contains invalid characters.");
        }
    }

    private static boolean emptyName(String name) {
        return name == null || name.trim().isEmpty();
    }

    private static boolean notValidLength(String name) {
        return name.length() < 3 || name.length() > 20;
    }
}
