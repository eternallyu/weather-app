package ru.eternallyu.exception;

public class LocationNotFoundException extends WeatherApiException {
    public LocationNotFoundException(String message) {
        super(message);
    }
}
