package com.project.rideEase.repositories;

import com.project.rideEase.entities.Driver;
import com.project.rideEase.entities.User;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver,Long> {

    @Query(value = "SELECT d.*, ST_Distance(d.current_location, :pickUpLocation) as distance "+
            "FROM driver as d "+
            "WHERE d.available AND ST_DWithin(d.current_location,:pickUpLocation,10000) "+
            "ORDER BY distance "+
            "LIMIT 10", nativeQuery = true
    )
    List<Driver> findTenNearestDrivers(Point pickUpLocation);

    @Query(value = "SELECT d.*, ST_Distance(d.current_location, :pickUpLocation) as distance "+
            "FROM driver as d "+
            "WHERE d.available AND ST_DWithin(d.current_location, :pickUpLocation, 15000) "+
            "ORDER BY BY d.rating DESC, distance ASC "+
            "LIMIT 10", nativeQuery = true)
    List<Driver> findNearByTopRatedDrivers(Point pickUpLocation);

    Optional<Driver> findByUser(User user);
}
