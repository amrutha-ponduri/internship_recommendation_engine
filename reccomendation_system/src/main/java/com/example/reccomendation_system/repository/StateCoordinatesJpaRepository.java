package com.example.reccomendation_system.repository;

import com.example.reccomendation_system.dto.LocationCoordinates;
import com.example.reccomendation_system.dto.LocationDropDownItemDTO;
import com.example.reccomendation_system.model.StateCoordinates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateCoordinatesJpaRepository extends JpaRepository<StateCoordinates, Integer> {
    @Query("""
            SELECT new com.example.reccomendation_system.dto.LocationCoordinates(s.stateName, s.latitude, s.longitude)
             FROM StateCoordinates s WHERE s.stateName = :state""")
    List<LocationCoordinates> findStateCoordinatesByStateName(@Param("state") String state);

    @Query("""
            SELECT new com.example.reccomendation_system.dto.LocationDropDownItemDTO(s.id, s.stateName)
            FROM StateCoordinates s""")
    List<LocationDropDownItemDTO> findLocationDropDownItems();
}
