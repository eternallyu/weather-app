package ru.eternallyu.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coordinates {

    @JsonProperty("lon")
    private BigDecimal longitude;

    @JsonProperty("lat")
    private BigDecimal latitude;
}
