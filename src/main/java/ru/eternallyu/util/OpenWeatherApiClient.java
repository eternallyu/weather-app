package ru.eternallyu.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.eternallyu.dto.SearchLocationDto;
import ru.eternallyu.dto.weather.WeatherDto;
import ru.eternallyu.exception.LocationNotFoundException;
import ru.eternallyu.exception.WeatherApiException;
import ru.eternallyu.exception.WeatherNotFoundException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Component
public class OpenWeatherApiClient {

    private static final HttpClient client = HttpClient.newHttpClient();

    private final Environment environment;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public OpenWeatherApiClient(Environment environment) {
        this.environment = environment;
    }

    public List<SearchLocationDto> getLocationsByName(String name) {
        String url = environment.getProperty("openweather.api.geo.url") +
                     "?q=" + name +
                     "&limit=" + environment.getProperty("openweather.api.geo.max-results") +
                     "&appid=" + environment.getProperty("openweather.api.key");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 404) {
                throw new LocationNotFoundException("Location not found.");
            }

            if (response.statusCode() != 200) {
                throw new WeatherApiException("API error.");
            }

            return objectMapper.readValue(
                    response.body(),
                    new TypeReference<List<SearchLocationDto>>() {
                    }
            );
        } catch (IOException | InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }

    public WeatherDto getWeatherByCoordinates(BigDecimal latitude, BigDecimal longitude) {
        String url = environment.getProperty("openweather.api.weather.url") +
                     "?lat=" + latitude +
                     "&lon=" + longitude +
                     "&appid=" + environment.getProperty("openweather.api.key") +
                     "&units=metric";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 404) {
                throw new WeatherNotFoundException("Location not found.");
            }

            if (response.statusCode() != 200) {
                throw new WeatherApiException("API error.");
            }

            return objectMapper.readValue(
                    response.body(),
                    WeatherDto.class
            );

        } catch (IOException | InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }
}
