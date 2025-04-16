package ru.eternallyu.dto.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TemperatureInfo {

    @JsonProperty("temp")
    private String temp;

    @JsonProperty("feels_like")
    private String feelsLike;
}
