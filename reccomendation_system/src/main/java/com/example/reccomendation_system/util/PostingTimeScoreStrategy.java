package com.example.reccomendation_system.util;

import com.example.reccomendation_system.dto.UserRequirements;
import com.example.reccomendation_system.repository.InternshipJpaRepository;
import com.example.reccomendation_system.repository.ScoreStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;

@Component
public class PostingTimeScoreStrategy implements ScoreStrategy {

    private final InternshipJpaRepository internshipJpaRepository;
    private final DefaultScoreStrategy defaultScoreStrategy;

    @Autowired
    public PostingTimeScoreStrategy(InternshipJpaRepository internshipJpaRepository, DefaultScoreStrategy defaultScoreStrategy) {
        this.internshipJpaRepository = internshipJpaRepository;
        this.defaultScoreStrategy = defaultScoreStrategy;
    }

    @Override
    public void apply(List<Integer> eligibleInternshipIds, HashMap<Integer, Double> preferenceScores, UserRequirements userRequirements, double weight) {
        List<Object[]> results = internshipJpaRepository.findMaxAndMinPostingTimeDifference(eligibleInternshipIds);
        List<Object[]> records = internshipJpaRepository.findAllPostingTimeDifferenceById(eligibleInternshipIds);
        if (results.isEmpty() || records.isEmpty()) {
            return;
        }
        Object[] maxAndMinPostingDiffs = results.get(0);
        long maxPostingTimeDifference = ((Number) (maxAndMinPostingDiffs[0])).longValue(), minPostingTimeDifference = ((Number)(maxAndMinPostingDiffs[1])).longValue();
        if (maxPostingTimeDifference == minPostingTimeDifference) {
            defaultScoreStrategy.apply(eligibleInternshipIds, preferenceScores, userRequirements, weight);
        } else {
            for (Object[] record : records) {
                int id = ((Number)record[0]).intValue();
                long currPostingTimeDifference = ((Number) record[1]).longValue();
                double score = ((double) maxPostingTimeDifference - currPostingTimeDifference) / ((double) maxPostingTimeDifference - minPostingTimeDifference);
                preferenceScores.put(id, preferenceScores.getOrDefault(id, 0.0) + score * weight);
            }
        }
    }
}
