package ru.eternallyu;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.eternallyu.dto.SearchLocationDto;
import ru.eternallyu.exception.NotFoundException;
import ru.eternallyu.util.OpenWeatherApiClient;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application-test.properties")
@ActiveProfiles("test")
@ContextConfiguration(classes = {
        OpenWeatherApiClient.class,
        OpenWeatherApiClientIntegrationTest.HttpClientConfig.class
})
public class OpenWeatherApiClientIntegrationTest {

    @Configuration
    static class HttpClientConfig {
        @Bean
        public HttpClient httpClient() {
            return Mockito.mock(HttpClient.class);
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private OpenWeatherApiClient apiClient;

    private String geoJson;

    private String LOCATION_NAME = "London";

    @BeforeEach
    public void setUp() throws IOException, InterruptedException {
        geoJson = Files.readString(Path.of("src/test/resources/geo_success.json"));

        HttpResponse<String> mockResponse = (HttpResponse<String>) Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        Mockito.when(mockResponse.body()).thenReturn(geoJson);

        Mockito.when(
                httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString()))
        ).thenReturn(mockResponse);
    }

    @Test
    public void getLocationsByName() {
        List<SearchLocationDto> locations = apiClient.getLocationsByName(LOCATION_NAME);

        Assertions.assertThat(locations.size()).isGreaterThan(0);

        SearchLocationDto location = locations.getFirst();
        Assertions.assertThat(location.getName()).isEqualTo(LOCATION_NAME);
    }

    @Test
    public void badRequest() throws IOException, InterruptedException {
        HttpResponse<String> response404 = (HttpResponse<String>) Mockito.mock(HttpResponse.class);
        Mockito.when(response404.statusCode()).thenReturn(404);

        Mockito.when(
                httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString()))
        ).thenReturn(response404);

        org.junit.jupiter.api.Assertions.assertThrows(NotFoundException.class, () -> apiClient.getLocationsByName(LOCATION_NAME));
    }
}
