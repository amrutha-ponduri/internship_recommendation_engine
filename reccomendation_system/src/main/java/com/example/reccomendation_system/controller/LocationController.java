package com.example.reccomendation_system.controller;

import com.example.reccomendation_system.dto.LocationDropDownItemDTO;
import com.example.reccomendation_system.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {
    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/")
    public List<LocationDropDownItemDTO> getLocationList() {
        return locationService.getLocationList();
    }
}
