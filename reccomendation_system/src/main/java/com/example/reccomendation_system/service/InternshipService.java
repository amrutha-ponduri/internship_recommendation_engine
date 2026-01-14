package com.example.reccomendation_system.service;

import com.example.reccomendation_system.dto.InternshipDTO;
import com.example.reccomendation_system.mapper.Mapper;
import com.example.reccomendation_system.model.Internship;
import com.example.reccomendation_system.repository.InternshipJpaRepository;
import com.example.reccomendation_system.repository.InternshipRepository;
import com.example.reccomendation_system.util.EligibilityFiltering;
import com.example.reccomendation_system.util.FinalInternshipScoring;
import com.example.reccomendation_system.util.MlModelScores;
import com.example.reccomendation_system.util.PreferenceScoreCalculator;
import com.example.reccomendation_system.dto.UserRequirements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

@Service
public class InternshipService implements InternshipRepository {

    private final InternshipJpaRepository internshipJpaRepository;
    private final Mapper mapper;
    private final PreferenceScoreCalculator preferenceScoreCalculator;
    private final EligibilityFiltering eligibilityFiltering;
    private final MlModelScores mlModelScores;
    private final FinalInternshipScoring finalInternshipScoring;

    @Autowired
    public InternshipService(InternshipJpaRepository internshipJpaRepository, Mapper mapper, PreferenceScoreCalculator preferenceScoreCalculator, EligibilityFiltering eligibilityFiltering, MlModelScores mlModelScores, FinalInternshipScoring finalInternshipScoring) {
        this.internshipJpaRepository = internshipJpaRepository;
        this.mapper = mapper;
        this.preferenceScoreCalculator = preferenceScoreCalculator;
        this.eligibilityFiltering = eligibilityFiltering;
        this.mlModelScores = mlModelScores;
        this.finalInternshipScoring = finalInternshipScoring;
    }

    @Override
    public ArrayList<InternshipDTO> getInternships() {
        List<Internship> internshipList = internshipJpaRepository.findAll();
        ArrayList<InternshipDTO> internshipDTOS = new ArrayList<>();
        for (Internship internship : internshipList) {
            internshipDTOS.add(mapper.toInternshipDTO(internship));
        }
        return internshipDTOS;
    }

    public ArrayList<InternshipDTO> getTopFiveInternships(int userId, UserRequirements userRequirements) {
        ArrayList<Integer> eligibleInternshipIds = eligibilityFiltering.getEligibleInternshipIds(userId);
        HashMap<Integer, Double> preferenceScores = preferenceScoreCalculator.getPreferenceScores(eligibleInternshipIds, userRequirements);
        HashMap<Integer, Double> mlModel1Scores = mlModelScores.getMLScores(userId, eligibleInternshipIds);
        HashMap<Integer, Double> finalScores = finalInternshipScoring.getFinalScores(eligibleInternshipIds, preferenceScores, userRequirements, mlModel1Scores);
        PriorityQueue<Integer> finalScoresOrderedQueue = new PriorityQueue<>((a, b) -> {
            return Double.compare(finalScores.getOrDefault(b, 0.0), finalScores.getOrDefault(a, 0.0));
        });
        finalScoresOrderedQueue.addAll(finalScores.keySet());
        ArrayList<InternshipDTO> rankedInternshipDTOS = new ArrayList<>();
        int count = 0;
        while (!finalScoresOrderedQueue.isEmpty() && count < 5) {
            int id = finalScoresOrderedQueue.poll();
            InternshipDTO internshipDTO = mapper.toInternshipDTO(internshipJpaRepository.findById(id).get());
            rankedInternshipDTOS.add(internshipDTO);
            count++;
        }
        return rankedInternshipDTOS;
    }
}
