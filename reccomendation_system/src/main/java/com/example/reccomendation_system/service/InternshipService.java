package com.example.reccomendation_system.service;

import com.example.reccomendation_system.dto.InternshipDTO;
import com.example.reccomendation_system.dto.ProjectExperienceDescription;
import com.example.reccomendation_system.dto.UserRequirements;
import com.example.reccomendation_system.dto.UserRequirementsAndProjectExperienceDescription;
import com.example.reccomendation_system.mapper.Mapper;
import com.example.reccomendation_system.model.Internship;
import com.example.reccomendation_system.repository.InternshipJpaRepository;
import com.example.reccomendation_system.repository.InternshipRepository;
import com.example.reccomendation_system.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InternshipService implements InternshipRepository {

    private final ShortlistingAndPreferenceScoring shortlistingAndPreferenceScoring;
    private final InternshipJpaRepository internshipJpaRepository;
    private final Mapper mapper;
    private final GeminiScoring geminiScoring;

    @Autowired
    public InternshipService(ShortlistingAndPreferenceScoring shortlistingAndPreferenceScoring, InternshipJpaRepository internshipJpaRepository, Mapper mapper, PreferenceAndPriorityScoreCalculator preferenceAndPriorityScoreCalculator, EligibilityFiltering eligibilityFiltering, MlModelScores mlModelScores, FinalInternshipScoring finalInternshipScoring, GeminiScoring geminiScoring) {
        this.shortlistingAndPreferenceScoring = shortlistingAndPreferenceScoring;
        this.internshipJpaRepository = internshipJpaRepository;
        this.mapper = mapper;
        this.geminiScoring = geminiScoring;
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

    @Override
    public ArrayList<InternshipDTO> getTopFiveInternships(int userId, UserRequirementsAndProjectExperienceDescription userRequirementsAndProjectExperienceDescription) throws InterruptedException {
        UserRequirements userRequirements = userRequirementsAndProjectExperienceDescription.getUserRequirements();
        ProjectExperienceDescription projectExperienceDescription = userRequirementsAndProjectExperienceDescription.getProjectExperienceDescription();
        if (projectExperienceDescription.getExperienceDescription() == null) {
            projectExperienceDescription.setExperienceDescription("");
        }
        if (projectExperienceDescription.getProjectDescription() == null) {
            projectExperienceDescription.setProjectDescription("");
        }
        HashMap<Integer, Double> topFiveInternshipsShortlistingScores = shortlistingAndPreferenceScoring.getTopFiveInternshipIdsAndScores(userId, userRequirements);
        ArrayList<Integer> topFiveShortlistedInternships = new ArrayList<>(topFiveInternshipsShortlistingScores.keySet());

        HashMap<Integer, Double> selectionScores = geminiScoring.getGeminiScores(projectExperienceDescription, topFiveShortlistedInternships);
        HashMap<Integer, Double> copyScores = new HashMap<>(selectionScores);

        if (selectionScores == null || selectionScores.isEmpty()) {
            return new ArrayList<>(internshipJpaRepository.findAllInternshipsByInternshipIds(topFiveShortlistedInternships));
        }

        for (Map.Entry<Integer, Double> entry : copyScores.entrySet()) {
            if (entry.getValue() == -1.0) {
                int id = entry.getKey();
                selectionScores.put(id, topFiveInternshipsShortlistingScores.get(id));
            }
        }

        ArrayList<Integer> top5InternshipIds = new ArrayList<>();
        PriorityQueue<Integer> orderedQueue = new PriorityQueue<>((a, b) -> Double.compare(selectionScores.getOrDefault(b, 0.0), selectionScores.getOrDefault(a, 0.0)));
        orderedQueue.addAll(selectionScores.keySet());
        int count = 0;
        while (!orderedQueue.isEmpty() && count < 5) {
            int id = orderedQueue.poll();
            top5InternshipIds.add(id);
            count++;
        }

        return new ArrayList<>(internshipJpaRepository.findAllInternshipsByInternshipIds(top5InternshipIds));
    }
}
