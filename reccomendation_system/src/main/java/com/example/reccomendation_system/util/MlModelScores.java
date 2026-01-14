package com.example.reccomendation_system.util;

import com.example.reccomendation_system.dto.ml_model_dtos.CompleteInfoDTO;
import com.example.reccomendation_system.dto.ml_model_dtos.InternshipInfoDTO;
import com.example.reccomendation_system.dto.ml_model_dtos.ScoredInternshipDTO;
import com.example.reccomendation_system.dto.ml_model_dtos.UserInfoDTO;
import com.example.reccomendation_system.mapper.Mapper;
import com.example.reccomendation_system.model.Internship;
import com.example.reccomendation_system.model.InternshipRequirements;
import com.example.reccomendation_system.model.User;
import com.example.reccomendation_system.repository.InternshipJpaRepository;
import com.example.reccomendation_system.repository.InternshipRequirementsJpaRepository;
import com.example.reccomendation_system.repository.MlModelRepository;
import com.example.reccomendation_system.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class MlModelScores implements MlModelRepository {

    private final RestTemplate restTemplate;
    private final InternshipJpaRepository internshipJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final InternshipRequirementsJpaRepository internshipRequirementsJpaRepository;
    private final Mapper mapper;

    @Autowired
    public MlModelScores(RestTemplate restTemplate, InternshipJpaRepository internshipJpaRepository, UserJpaRepository userJpaRepository, InternshipRequirementsJpaRepository internshipRequirementsJpaRepository, Mapper mapper) {
        this.restTemplate = restTemplate;
        this.internshipJpaRepository = internshipJpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.internshipRequirementsJpaRepository = internshipRequirementsJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public HashMap<Integer, Double> getMLScores(int userId, List<Integer> eligibleInternshipIds) {

        User user = userJpaRepository.findById(userId).get();
        UserInfoDTO userInfoDTO = mapper.toUserInfoDTO(user);

        ArrayList<Internship> internshipList = new ArrayList<>(internshipJpaRepository.findAllById(eligibleInternshipIds));
        HashMap<Integer, Double> mlModel1Scores = new HashMap<>();

        for (int i = 0; i < Math.ceil(internshipList.size() / 50.0); i++) {

            ArrayList<InternshipInfoDTO> internshipInfoDTOs = new ArrayList<>();
            for (int j = 0; j < 50 && (i * 50 + j) < internshipList.size() ; j++) {
                Internship internship = internshipList.get(i*50 + j);
                int id = internship.getId();
                InternshipRequirements internshipRequirements = internshipRequirementsJpaRepository.findById(id).get();
                InternshipInfoDTO internshipInfoDTO = mapper.toInternshipInfoDTO(internship, internshipRequirements);
                internshipInfoDTOs.add(internshipInfoDTO);
            }

            ArrayList<ScoredInternshipDTO> scoredInternshipDTOS = getScoredInternships(new CompleteInfoDTO(userInfoDTO, internshipInfoDTOs));
            for (ScoredInternshipDTO scoredInternshipDTO : scoredInternshipDTOS) {
                mlModel1Scores.put(scoredInternshipDTO.getInternshipId(), scoredInternshipDTO.getProbability());
            }

        }

        return mlModel1Scores;
    }


    @Override
    public ArrayList<ScoredInternshipDTO> getScoredInternships(CompleteInfoDTO completeInfo) {

        String mlModelUrl = "http://127.0.0.1:8000/batch_predict/";

        try {
            // Prepare headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Wrap the object directly; RestTemplate will handle serialization
            HttpEntity<CompleteInfoDTO> request = new HttpEntity<>(completeInfo, headers);

            // Send POST request and get response
            ResponseEntity<ArrayList<ScoredInternshipDTO>> response = restTemplate.exchange(
                    mlModelUrl,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<ArrayList<ScoredInternshipDTO>>() {}
            );

            return response.getBody() != null ? response.getBody() : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


}
