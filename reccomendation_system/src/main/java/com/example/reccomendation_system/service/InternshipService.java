package com.example.reccomendation_system.service;

import com.example.reccomendation_system.dto.*;
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
    public ArrayList<ScoredInternshipDTO> getTopFiveInternships(int userId, UserRequirementsAndProjectExperienceDescription userRequirementsAndProjectExperienceDescription) throws InterruptedException {
        UserRequirements userRequirements = userRequirementsAndProjectExperienceDescription.getUserRequirements();
        ProjectExperienceDescription projectExperienceDescription = getProjectExperienceDescription(userRequirementsAndProjectExperienceDescription);
        Map<Integer, Double> topFiveInternshipsShortlistingScores = shortlistingAndPreferenceScoring.getTopFiveInternshipIdsAndScores(userId, userRequirements);
        ArrayList<Integer> topFiveShortlistedInternships = new ArrayList<>(topFiveInternshipsShortlistingScores.keySet());

        HashMap<Integer, Double> selectionScores = geminiScoring.getGeminiScores(projectExperienceDescription, topFiveShortlistedInternships);
        HashMap<Integer, Double> copyScores = new HashMap<>(selectionScores);

        if (selectionScores.isEmpty()) {
            ArrayList<ScoredInternshipDTO> scoredInternshipDTOS = new ArrayList<>(internshipJpaRepository.findAllInternshipsByInternshipIds(topFiveShortlistedInternships));
            for (ScoredInternshipDTO scoredInternshipDTO : scoredInternshipDTOS) {
                scoredInternshipDTO.setScore(topFiveInternshipsShortlistingScores.get(scoredInternshipDTO.getInternshipId()));
            }
            Collections.sort(scoredInternshipDTOS, (a, b) -> Double.compare(b.getScore(), a.getScore()));
            return scoredInternshipDTOS;
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

        ArrayList<ScoredInternshipDTO> internships = new ArrayList<>(internshipJpaRepository.findAllInternshipsByInternshipIds(top5InternshipIds));
        for (ScoredInternshipDTO scoredInternshipDTO : internships) {
            scoredInternshipDTO.setScore(selectionScores.get(scoredInternshipDTO.getInternshipId()));
        }
        Collections.sort(internships, (a, b) -> Double.compare(b.getScore(), a.getScore()));
        return internships;
    }

    private static ProjectExperienceDescription getProjectExperienceDescription(UserRequirementsAndProjectExperienceDescription userRequirementsAndProjectExperienceDescription) {
        ProjectExperienceDescription projectExperienceDescription = userRequirementsAndProjectExperienceDescription.getProjectExperienceDescription();
        if (projectExperienceDescription == null) {
            projectExperienceDescription = new ProjectExperienceDescription("", "");
        } else {
            if (projectExperienceDescription.getExperienceDescription() == null) {
                projectExperienceDescription.setExperienceDescription("");
            }
            if (projectExperienceDescription.getProjectDescription() == null) {
                projectExperienceDescription.setProjectDescription("");
            }
        }
        return projectExperienceDescription;
    }
}
