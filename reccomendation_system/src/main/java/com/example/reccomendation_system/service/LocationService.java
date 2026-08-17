package com.example.reccomendation_system.service;

import com.example.reccomendation_system.dto.LocationDropDownItemDTO;
import com.example.reccomendation_system.repository.StateCoordinatesJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    private final StateCoordinatesJpaRepository stateCoordinatesJpaRepository;

    @Autowired
    public LocationService(StateCoordinatesJpaRepository stateCoordinatesJpaRepository) {
        this.stateCoordinatesJpaRepository = stateCoordinatesJpaRepository;
    }

    public List<LocationDropDownItemDTO> getLocationList() {
        return stateCoordinatesJpaRepository.findLocationDropDownItems();
    }
}
