package com.example.reccomendation_system.util;

import com.example.reccomendation_system.dto.UserRequirements;
import com.example.reccomendation_system.repository.InternshipJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

@Component
public class ShortlistingAndPreferenceScoring {

    private final EligibilityFiltering eligibilityFiltering;
    private final PreferenceAndPriorityScoreCalculator preferenceAndPriorityScoreCalculator;
    private final MlModelScores mlModelScores;
    private final FinalInternshipScoring finalInternshipScoring;

    @Autowired
    public ShortlistingAndPreferenceScoring(EligibilityFiltering eligibilityFiltering, PreferenceAndPriorityScoreCalculator preferenceAndPriorityScoreCalculator, MlModelScores mlModelScores, FinalInternshipScoring finalInternshipScoring, InternshipJpaRepository internshipJpaRepository) {
        this.eligibilityFiltering = eligibilityFiltering;
        this.preferenceAndPriorityScoreCalculator = preferenceAndPriorityScoreCalculator;
        this.mlModelScores = mlModelScores;
        this.finalInternshipScoring = finalInternshipScoring;
    }

    public HashMap<Integer, Double> getTopFiveInternshipIdsAndScores(int userId, UserRequirements userRequirements) {
        ArrayList<Integer> eligibleInternshipIds = eligibilityFiltering.getEligibleInternshipIds(userId);
        HashMap<Integer, Double> preferenceScores = preferenceAndPriorityScoreCalculator.getPreferenceScores(eligibleInternshipIds, userRequirements);
        HashMap<Integer, Double> mlModel1Scores = mlModelScores.getMLScores(userId, eligibleInternshipIds);
        HashMap<Integer, Double> finalScores = finalInternshipScoring.getFinalScores(eligibleInternshipIds, preferenceScores, userRequirements, mlModel1Scores);

        PriorityQueue<Integer> shortlistingScoresOrderedQueue = new PriorityQueue<>((a, b) -> Double.compare(finalScores.getOrDefault(b, 0.0), finalScores.getOrDefault(a, 0.0)));
        shortlistingScoresOrderedQueue.addAll(finalScores.keySet());

        int count = 0, maxCount = 5;

        HashMap<Integer, Double> topFiveInternshipShorlistingScores = new HashMap<>();

        while (!shortlistingScoresOrderedQueue.isEmpty() && count < maxCount) {
            int id = shortlistingScoresOrderedQueue.poll();
            topFiveInternshipShorlistingScores.put(id, finalScores.get(id));
            count++;
        }
        return topFiveInternshipShorlistingScores;
    }
}
