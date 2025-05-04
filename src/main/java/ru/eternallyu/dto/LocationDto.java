package ru.eternallyu.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class LocationDto {
    private String name;
    private Long userId;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
