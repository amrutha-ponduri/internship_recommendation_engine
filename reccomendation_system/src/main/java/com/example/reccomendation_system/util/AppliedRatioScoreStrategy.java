package com.example.reccomendation_system.util;

import com.example.reccomendation_system.dto.UserRequirements;
import com.example.reccomendation_system.repository.InternshipJpaRepository;
import com.example.reccomendation_system.repository.ScoreStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class AppliedRatioScoreStrategy implements ScoreStrategy {

    private final InternshipJpaRepository internshipJpaRepository;
    private final DefaultScoreStrategy defaultScoreStrategy;

    @Autowired
    public AppliedRatioScoreStrategy(InternshipJpaRepository internshipJpaRepository, DefaultScoreStrategy defaultScoreStrategy) {
        this.internshipJpaRepository = internshipJpaRepository;
        this.defaultScoreStrategy = defaultScoreStrategy;
    }

    @Override
    public void apply(List<Integer> eligibleInternshipIds, HashMap<Integer, Double> preferenceScores, UserRequirements userRequirements, double weight) {
        List<Object[]> results = internshipJpaRepository.findMaxMinRatiosAndTotalCounts(eligibleInternshipIds);
        if (results == null || results.isEmpty()) {
            return;
        }
        Object[] maxMinRatiosAndTotalCounts = results.get(0);
        double maxAppliedRatio = ((Number) maxMinRatiosAndTotalCounts[0]).doubleValue();
        double minAppliedRatio = ((Number) maxMinRatiosAndTotalCounts[1]).doubleValue();
        int maxTotalCount = ((Number) maxMinRatiosAndTotalCounts[2]).intValue();
        int minTotalCount = ((Number) maxMinRatiosAndTotalCounts[3]).intValue();
        if (maxAppliedRatio == minAppliedRatio && maxTotalCount == minTotalCount) {
            defaultScoreStrategy.apply(eligibleInternshipIds, preferenceScores, userRequirements, weight);
            return;
        }
        List<Object[]> appliedCountAndAppliedRatios = internshipJpaRepository.findAllTotalCountsAndAppliedRatiosById(eligibleInternshipIds);
        HashMap<Integer, Double> appliedRatiosMap = new HashMap<>();
        HashMap<Integer, Integer> totalCountsMap = new HashMap<>();

        for (Object[] record : appliedCountAndAppliedRatios) {
            int internshipId = ((Number) record[0]).intValue();
            totalCountsMap.put(internshipId, ((Number) record[1]).intValue());
            if (record[2] != null) {
                appliedRatiosMap.put(internshipId, ((Number) record[2]).doubleValue());
            }
        }
        for (int internshipId : eligibleInternshipIds) {
            double score = 0.0;
            if (!appliedRatiosMap.containsKey(internshipId)) {
                if (maxTotalCount != minTotalCount) {
                    int currTotalCount = totalCountsMap.getOrDefault(internshipId, 0);
                    score = ((double)currTotalCount - minTotalCount) / (maxTotalCount - minTotalCount);
                }
            }
            else if (maxAppliedRatio != minAppliedRatio) {
                double currAppliedRatio = appliedRatiosMap.get(internshipId);
                score = (maxAppliedRatio - currAppliedRatio) / (maxAppliedRatio - minAppliedRatio);
            }
            preferenceScores.put(internshipId, score * weight + preferenceScores.getOrDefault(internshipId, 0.0));
        }
    }
}
