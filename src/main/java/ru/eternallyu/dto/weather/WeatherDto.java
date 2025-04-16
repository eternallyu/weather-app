package ru.eternallyu.dto.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherDto {

    private Long id;

    @JsonProperty("coord")
    private Coordinates coordinates;

    @JsonProperty("weather")
    private List<WeatherCondition> weather;

    @JsonProperty("main")
    private TemperatureInfo temp;

    @JsonProperty("sys")
    private SysWeatherInfo sys;

    @JsonProperty("name")
    private String name;
}
