package ru.eternallyu.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
public class LocationDto {
    private String name;
    private Integer userId;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
