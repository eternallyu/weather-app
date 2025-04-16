package ru.eternallyu.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coordinates {

    @JsonProperty("lon")
    private BigDecimal longitude;

    @JsonProperty("lat")
    private BigDecimal latitude;
}
