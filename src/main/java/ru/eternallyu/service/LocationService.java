package ru.eternallyu.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.eternallyu.dto.LocationDto;
import ru.eternallyu.dto.SearchLocationDto;
import ru.eternallyu.dto.weather.WeatherDto;
import ru.eternallyu.exception.InvalidResourceException;
import ru.eternallyu.mapper.LocationMapper;
import ru.eternallyu.model.entity.Location;
import ru.eternallyu.repository.LocationRepository;
import ru.eternallyu.util.OpenWeatherApiClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    private final OpenWeatherApiClient openWeatherApiClient;

    private final LocationMapper locationMapper;
    
    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    @Transactional
    public void deleteLocationById(Long locationId, Long userId) {
        Location location = locationRepository.findByIdAndUserId(locationId, userId)
                .orElseThrow(() -> new InvalidResourceException("Location not found or doesn't belong to user."));
        locationRepository.delete(location);
    }

    public List<SearchLocationDto> getLocationsByName(String name) {
        return openWeatherApiClient.getLocationsByName(name);
    }

    public void createLocation(LocationDto locationDto) {
        Location location = locationMapper.mapDtoToLocation(locationDto);

        locationRepository.save(location);
    }

    @Transactional
    public List<WeatherDto> getWeatherForUserLocationsByUserId(Long userId) {
        List<Location> locations = locationRepository.findByUserId(userId);
        List<WeatherDto> weatherDtoList = new ArrayList<>();

        for (Location location : locations) {
            WeatherDto weatherDto = openWeatherApiClient.getWeatherByCoordinates(
                    location.getLatitude(),
                    location.getLongitude()
            );

            weatherDto.setName(location.getName());
            weatherDto.setId(location.getId());
            weatherDtoList.add(weatherDto);
        }

        return weatherDtoList;
    }

    public void isUserAlreadyHasLocation(Long userId, String name, BigDecimal latitude, BigDecimal longitude) {
        if (locationRepository.findByUserIdAndNameAndLatitudeAndLongitude(userId, name, latitude, longitude).isPresent()) {
            logger.warn("User already has this location.");
            throw new InvalidResourceException("User already has this location.");
        };
    }
}
