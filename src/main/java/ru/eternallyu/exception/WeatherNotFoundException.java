package ru.eternallyu.exception;

public class WeatherNotFoundException extends WeatherApiException {
    public WeatherNotFoundException(String message) {
        super(message);
    }
}
