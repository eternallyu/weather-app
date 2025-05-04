package ru.eternallyu.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import ru.eternallyu.dto.SearchLocationDto;
import ru.eternallyu.dto.weather.WeatherDto;
import ru.eternallyu.exception.NotFoundException;
import ru.eternallyu.exception.WeatherApiException;
import ru.eternallyu.service.SessionService;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OpenWeatherApiClient {

    private final HttpClient client;

    private final Environment environment;

    private final ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(OpenWeatherApiClient.class);

    public List<SearchLocationDto> getLocationsByName(String name) {

        URI baseUrl = URI.create(Objects.requireNonNull(environment.getProperty("openweather.api.geo.url")));
        String limit = environment.getProperty("openweather.api.geo.max-results");
        String apiKey = environment.getProperty("openweather.api.key");

        URI uri = UriComponentsBuilder
                .fromUri(baseUrl)
                .queryParam("q", name)
                .queryParam("limit", limit)
                .queryParam("appid", apiKey)
                .build()
                .toUri();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> response = sendResponse(request);

            return objectMapper.readValue(
                    response.body(),
                    new TypeReference<List<SearchLocationDto>>() {
                    }
            );
        } catch (IOException | InterruptedException exception) {
            logger.error("Weather API error: {}", exception.getMessage());
            throw new WeatherApiException("Exception on Weather API end occurred for some unknown reason.");
        }
    }

    public WeatherDto getWeatherByCoordinates(BigDecimal latitude, BigDecimal longitude) {

        URI baseUrl = URI.create(Objects.requireNonNull(environment.getProperty("openweather.api.weather.url")));
        String apiKey = environment.getProperty("openweather.api.key");
        String unitsParam = "metric";

        URI uri = UriComponentsBuilder
                .fromUri(baseUrl)
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .queryParam("appid", apiKey)
                .queryParam("units", unitsParam)
                .build()
                .toUri();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> response = sendResponse(request);

            return objectMapper.readValue(
                    response.body(),
                    WeatherDto.class
            );

        } catch (IOException | InterruptedException exception) {
            logger.error("Weather API error: {}", exception.getMessage());
            throw new WeatherApiException("Exception on Weather API end occurred for some unknown reason.");
        }
    }

    private HttpResponse<String> sendResponse(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 404) {
            logger.warn("Location not found.");
            throw new NotFoundException("Location not found.");
        }

        if (response.statusCode() != 200) {
            logger.error("Unexpected status code: {}", response.statusCode());
            throw new WeatherApiException("Exception on Weather API end occurred for some unknown reason.");
        }
        return response;
    }
}
