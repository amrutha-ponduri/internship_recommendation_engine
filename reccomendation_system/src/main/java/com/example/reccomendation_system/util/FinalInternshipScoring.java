package com.example.reccomendation_system.util;

import com.example.reccomendation_system.dto.UserRequirements;
import com.example.reccomendation_system.repository.InternshipJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class FinalInternshipScoring {

    private final InternshipJpaRepository internshipJpaRepository;

    @Autowired
    public FinalInternshipScoring(InternshipJpaRepository internshipJpaRepository) {
        this.internshipJpaRepository = internshipJpaRepository;
    }

    public HashMap<Integer, Double> getFinalScores(List<Integer> eligibleInternshipIds, HashMap<Integer, Double> preferenceScores, UserRequirements userRequirements, HashMap<Integer, Double> mlScores) {

        HashMap<Integer, Double> finalScores = new HashMap<>();
        List<Object[]> fieldRecords = internshipJpaRepository.findAllFieldsById(eligibleInternshipIds);
        if (fieldRecords.isEmpty()) {
            return finalScores;
        }
        for (Object[] fieldRecord : fieldRecords) {
            int id = ((Number) fieldRecord[1]).intValue();
            String field = fieldRecord[0].toString();
            double finalScore = preferenceScores.getOrDefault(id, 0.0) * 0.3 + mlScores.getOrDefault(id, 0.0) * 0.9;
            if (field != null && field.equals(userRequirements.getPreferredDomain())) {
                finalScore = 2 * finalScore;
            }
            finalScores.put(id, finalScore);
        }
        return finalScores;
    }
}
