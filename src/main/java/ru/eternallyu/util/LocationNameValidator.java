package ru.eternallyu.util;

import org.springframework.stereotype.Component;
import ru.eternallyu.exception.InvalidResourceException;

@Component
public class LocationNameValidator {
    public static void validateLocationName(String name) {
        String trimmedName = name == null ? "" : name.trim();
        if (trimmedName.isEmpty()
            || trimmedName.contains("  ")
            || notValidLength(trimmedName)
            || !trimmedName.matches("[A-Za-zА-Яа-я ]+")
        ) {
            throw new InvalidResourceException("Location name contains invalid characters.");
        }
    }

    private static boolean notValidLength(String name) {
        return name.length() < 3 || name.length() > 20;
    }
}
