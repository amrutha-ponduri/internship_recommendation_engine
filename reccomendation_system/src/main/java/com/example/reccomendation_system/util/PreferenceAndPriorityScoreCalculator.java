package com.example.reccomendation_system.util;

import com.example.reccomendation_system.dto.UserRequirements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Calculates internship preference scores based on mode match and popularity.
 * A higher score indicates closer alignment with the user's preferences.
 */

@Component
public class PreferenceAndPriorityScoreCalculator {

    private final LocationPreferenceScoreStrategy locationPreferenceScoreStrategy;
    private final ModePreferenceScoreStrategy modePreferenceScoreStrategy;
    private final PostingTimeScoreStrategy postingTimeScoreStrategy;
    private final AppliedRatioScoreStrategy appliedRatioScoreStrategy;

    @Autowired
    public PreferenceAndPriorityScoreCalculator(LocationPreferenceScoreStrategy locationPreferenceScoreStrategy, ModePreferenceScoreStrategy modePreferenceScoreStrategy, PostingTimeScoreStrategy postingTimeScoreStrategy, AppliedRatioScoreStrategy appliedRatioScoreStrategy) {
        this.locationPreferenceScoreStrategy = locationPreferenceScoreStrategy;
        this.modePreferenceScoreStrategy = modePreferenceScoreStrategy;
        this.postingTimeScoreStrategy = postingTimeScoreStrategy;
        this.appliedRatioScoreStrategy = appliedRatioScoreStrategy;
    }


    public HashMap<Integer, Double> getPreferenceScores(ArrayList<Integer> eligibleInternshipIds, UserRequirements userRequirements) {
        if (eligibleInternshipIds == null || eligibleInternshipIds.isEmpty()) {
            return null;
        }
        HashMap<Integer, Double> preferenceScores = new HashMap<>();
        // calculate preference score for mode
        if (userRequirements.getPreferredMode() != null) {
            getModePreferenceScores(eligibleInternshipIds, preferenceScores, userRequirements);
        } else {
            for (int id : eligibleInternshipIds) {
                preferenceScores.put(id, preferenceScores.getOrDefault(id, 0.0) + 1);
            }
        }
        // calculate scores for applied ratios (applied_count / total_count)
        getAppliedRatioScores(eligibleInternshipIds, preferenceScores, userRequirements);

        // calculate preference scores based on location
        if (userRequirements.getPreferredCity() != null || userRequirements.getPreferredState() != null) {
            getLocationPreferenceScores(eligibleInternshipIds, preferenceScores, userRequirements);
        } else {
            for (int id : eligibleInternshipIds) {
                preferenceScores.put(id, preferenceScores.getOrDefault(id, 0.0) + 1);
            }
        }
        // calculate scores based on posting time
        getPostingTimeScores(eligibleInternshipIds, preferenceScores, userRequirements);
        return preferenceScores;
    }

    // mode preference scores
    private void getModePreferenceScores(List<Integer> eligibleInternshipIds, HashMap<Integer, Double> preferenceScores, UserRequirements userRequirements) {
        modePreferenceScoreStrategy.apply(eligibleInternshipIds, preferenceScores, userRequirements, 1);
    }

    // applied ratio scores
    private void getAppliedRatioScores(List<Integer> eligibleInternshipIds, HashMap<Integer, Double> preferenceScores, UserRequirements userRequirements) {
        appliedRatioScoreStrategy.apply(eligibleInternshipIds, preferenceScores, userRequirements, 1);
    }

    // location preference scores
    private void getLocationPreferenceScores(List<Integer> eligibleInternshipIds, HashMap<Integer, Double> preferenceScores, UserRequirements userRequirements) {
        locationPreferenceScoreStrategy.apply(eligibleInternshipIds, preferenceScores, userRequirements, 1);
    }

    // posting time scores
    private void getPostingTimeScores(List<Integer> eligibleInternshipIds, HashMap<Integer, Double> preferenceScores, UserRequirements userRequirements) {
        postingTimeScoreStrategy.apply(eligibleInternshipIds, preferenceScores, userRequirements, 1);
    }
}
