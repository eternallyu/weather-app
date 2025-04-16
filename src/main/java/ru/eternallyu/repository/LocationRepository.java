package ru.eternallyu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.eternallyu.model.entity.Location;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    List<Location> findByUserId(Integer userId);

    Optional<Location> findByIdAndUserId(Long id, Integer userId);

    Optional<Location> findByUserIdAndNameAndLatitudeAndLongitude(Integer userId, String name, BigDecimal latitude, BigDecimal longitude);
}
