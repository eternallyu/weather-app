package ru.eternallyu.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.eternallyu.dto.LocationDto;
import ru.eternallyu.model.entity.Location;
import ru.eternallyu.model.entity.User;
import ru.eternallyu.service.UserService;

@Component
@RequiredArgsConstructor
public class LocationMapper {

    private final UserService userService;

    public LocationDto mapLocationToDto(Location location) {
        return buildLocationDto(location);
    }

    private static LocationDto buildLocationDto(Location location) {
        return LocationDto.builder()
                .name(location.getName())
                .userId(location.getUser().getId())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
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
