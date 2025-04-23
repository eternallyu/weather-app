package ru.eternallyu.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.eternallyu.dto.LocationDto;
import ru.eternallyu.model.entity.Location;
import ru.eternallyu.model.entity.User;
import ru.eternallyu.service.UserService;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class LocationMapper {

    private final UserService userService;

    public static LocationDto buildLocationDto(BigDecimal latitude, BigDecimal longitude, String name, Long userId) {
        return LocationDto.builder()
                .name(name)
                .userId(userId)
                .longitude(longitude)
                .latitude(latitude)
                .build();
    }

    public Location mapDtoToLocation(LocationDto locationDto) {
        User user = userService.getUserById(locationDto.getUserId());
        return new Location(locationDto.getName(),
                user,
                locationDto.getLatitude(),
                locationDto.getLongitude());
    }
}
