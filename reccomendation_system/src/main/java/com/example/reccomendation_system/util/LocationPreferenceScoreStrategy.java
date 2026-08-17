package com.example.reccomendation_system.util;

import com.example.reccomendation_system.dto.LocationCoordinates;
import com.example.reccomendation_system.dto.LocationDTO;
import com.example.reccomendation_system.dto.UserRequirements;
import com.example.reccomendation_system.helper.EuclideanDistanceCalculator;
import com.example.reccomendation_system.repository.InternshipJpaRepository;
import com.example.reccomendation_system.repository.ScoreStrategy;
import com.example.reccomendation_system.repository.StateCoordinatesJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class LocationPreferenceScoreStrategy implements ScoreStrategy {

    private final DefaultScoreStrategy defaultScoreStrategy;
    private final EuclideanDistanceCalculator euclideanDistanceCalculator;
    private final InternshipJpaRepository internshipJpaRepository;
    private final StateCoordinatesJpaRepository stateCoordinatesJpaRepository;

    @Autowired
    public LocationPreferenceScoreStrategy(DefaultScoreStrategy defaultScoreStrategy, EuclideanDistanceCalculator euclideanDistanceCalculator, InternshipJpaRepository internshipJpaRepository, StateCoordinatesJpaRepository stateCoordinatesJpaRepository) {
        this.defaultScoreStrategy = defaultScoreStrategy;
        this.euclideanDistanceCalculator = euclideanDistanceCalculator;
        this.internshipJpaRepository = internshipJpaRepository;
        this.stateCoordinatesJpaRepository = stateCoordinatesJpaRepository;
    }

    @Override
    public void apply(List<Integer> eligibleInternshipIds, HashMap<Integer, Double> preferenceScores, UserRequirements userRequirements, double weight) {
        System.out.println("----------------------------------------");
        System.out.println("Initial Preference Scores : " + preferenceScores);
        System.out.println("----------------------------------------");
        String preferredState = userRequirements.getPreferredState();
        LocationCoordinates userPreferredCoords = getStateLocationCoordinates(preferredState);
        if (userPreferredCoords == null) {
            throw new EntityNotFoundException("Invalid location: State " + preferredState + " doesn't exist in the coordinates database.\n");
        }
        List<LocationDTO> internshipsLocations = internshipJpaRepository.findAllLocationCoordinatesById(eligibleInternshipIds);
        HashMap<Integer, Double> internshipLocationDistanceMap = new HashMap<>();
        HashMap<String, Double> distanceCache = new HashMap<>();
        double maxDistance = 0, minDistance = Double.POSITIVE_INFINITY;

        // get the distance for each internship location from preferred location
        for (LocationDTO locationDTO : internshipsLocations) {
            double targetLatitude = locationDTO.getStateLatitude();
            double targetLongitude = locationDTO.getStateLongitude();
            String key = locationDTO.getStateName();
            if (distanceCache.containsKey(key)) {
                // use cached distances
                internshipLocationDistanceMap.put(locationDTO.getInternshipId(), distanceCache.get(key));
            } else {
                // calculate euclidean distance
                double distance = euclideanDistanceCalculator.calculateDistance(userPreferredCoords.getLatitude(), userPreferredCoords.getLongitude(), targetLatitude, targetLongitude);
                internshipLocationDistanceMap.put(locationDTO.getInternshipId(), distance);
                // cache the results
                distanceCache.put(key, distance);
                maxDistance = Math.max(maxDistance, distance);
                minDistance = Math.min(minDistance, distance);
            }
        }
        System.out.println(distanceCache);

        if (maxDistance != minDistance) {
            for (int id : eligibleInternshipIds) {
                double distanceScore = (maxDistance - internshipLocationDistanceMap.get(id)) / (maxDistance - minDistance);
                preferenceScores.put(id, distanceScore * weight + preferenceScores.getOrDefault(id, 0.0));
                System.out.println(id + " " + distanceScore);
            }
        } else {
            defaultScoreStrategy.apply(eligibleInternshipIds, preferenceScores, userRequirements, weight);
        }
        System.out.println("----------------------------------------");
        System.out.println("Final Preference Scores : " + preferenceScores);
        System.out.println("----------------------------------------");
    }

    private LocationCoordinates getStateLocationCoordinates(String stateName) {
        List<LocationCoordinates> locationCoordinates = stateCoordinatesJpaRepository.findStateCoordinatesByStateName(stateName);
        if (locationCoordinates.isEmpty()) {
            return null;
        }
        return locationCoordinates.get(0);
    }

}
