package com.example.reccomendation_system.util;

import com.example.reccomendation_system.dto.ProjectExperienceDescription;
import com.example.reccomendation_system.dto.gemini_dtos.GeminiOutputDTO;
import com.example.reccomendation_system.dto.gemini_dtos.InternshipDetailsGeminiQueryDTO;
import com.example.reccomendation_system.repository.InternshipRequirementsJpaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GeminiScoring {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final InternshipRequirementsJpaRepository internshipRequirementsJpaRepository;

    @Autowired
    public GeminiScoring(InternshipRequirementsJpaRepository internshipRequirementsJpaRepository) {
        this.internshipRequirementsJpaRepository = internshipRequirementsJpaRepository;
    }

    public HashMap<Integer, Double> getGeminiScores(ProjectExperienceDescription projectExperienceDescription, ArrayList<Integer> internshipIds) throws InterruptedException {
        HashMap<Integer, Double> geminiScores = new HashMap<>();

        if (projectExperienceDescription.getExperienceDescription().isEmpty() && projectExperienceDescription.getProjectDescription().isEmpty()) {
            for (int id : internshipIds) {
                geminiScores.put(id, 0.0);
            }
            return geminiScores;
        }

        Client client = Client.builder().apiKey(apiKey).build();

        ArrayList<InternshipDetailsGeminiQueryDTO> internships = new ArrayList<>(internshipRequirementsJpaRepository.findAllInternshipDetailsForGemini(internshipIds));

        Map<Integer, List<InternshipDetailsGeminiQueryDTO>> groupedInternships = internships.stream().collect(Collectors.groupingBy(InternshipDetailsGeminiQueryDTO::getInternshipId));

        System.out.println("Calling Gemini!!!!!!!");

        for (Map.Entry<Integer, List<InternshipDetailsGeminiQueryDTO>> entry : groupedInternships.entrySet()) {
            int internshipId = entry.getKey();
            List<InternshipDetailsGeminiQueryDTO> internshipDetails = entry.getValue();
            List<String> skillsList = internshipDetails.stream().map(InternshipDetailsGeminiQueryDTO::getSkillName).collect(Collectors.toList());
            String company = internshipDetails.get(0).getCompanyName();
            String internship_skills = String.join(", ", skillsList);
            String internship_role = internshipDetails.get(0).getInternshipRole();
            String projectDescription = projectExperienceDescription.getProjectDescription().isEmpty() ? "No projects done by the applicant" : "\nProjects : \n" + projectExperienceDescription.getProjectDescription();
            String experienceDescription = projectExperienceDescription.getExperienceDescription().isEmpty() ? "No prior experience" : "\nExperience : \n" + projectExperienceDescription.getExperienceDescription();
            String prompt = """
                    You are a strict and highly critical evaluator for the company """ + company + """
                    .
                    
                    Evaluate the candidate for this internship.
                    
                    The role is: """ + internship_role + """
                    Required skills: """ + internship_skills + projectDescription + experienceDescription +
                    """
                            
                            Rules:
                            - Be consistent and deterministic
                            - Do NOT guess randomly
                            - Use only the given data
                            - Be strict in evaluation
                            - Do NOT give benefit of doubt
                            
                            Strict Evaluation Guidelines:
                            - Only consider explicitly mentioned skills, tools, and outcomes
                            - Ignore vague claims like "worked on", "familiar with", "good knowledge" without proof
                            - Give higher scores ONLY if there is clear evidence (technologies, impact, complexity)
                            - Penalize lack of detail or unclear contributions
                            - Do NOT assume skills not explicitly stated
                            - If insufficient information is available, assign a low score
                            
                            Scoring:
                            - Project relevance: /10
                            - Experience level: /10
                            
                            Scoring Guidelines:
                            - 0–3: Very weak or irrelevant
                            - 4–6: Moderate relevance with limited evidence
                            - 7–8: Strong relevance with good supporting details
                            - 9–10: Excellent match with clear, strong evidence and impact
                            
                            Final score = (project_weight * project) + (experience * experience)
                            Adjust weights as per the internship requirements and the company.
                            
                            Return ONLY valid JSON:
                            {
                              "project": number,
                              "experience": number,
                              "final": number
                            }
                            Return strictly valid JSON. No markdown or extra text.
                            """;

            GenerateContentConfig generateContentConfig = GenerateContentConfig.builder().temperature(0.0f).build();
            ObjectMapper obj = new ObjectMapper();
            int retries = 0, maxRetries = 2;
            while (retries < maxRetries) {
                GenerateContentResponse response =
                        client.models.generateContent(
                                "gemini-2.5-flash",
                                prompt,
                                generateContentConfig);

                String json = response.text();
                json = json.replace("```json", "")
                        .replace("```", "")
                        .trim();
                try {
                    GeminiOutputDTO geminiOutputDTO = obj.readValue(json, GeminiOutputDTO.class);
                    geminiScores.put(internshipId, geminiOutputDTO.getScore());
                    break;
                } catch (JsonProcessingException e) {
                    // call gemini api again
                    prompt += "\nReturn strictly valid JSON. No formatting mistakes.";
                    retries++;
                }
            }
            if (retries == maxRetries) {
                geminiScores.put(internshipId, -1.0);
            }
        }
        System.out.println("Gemini call completed!!!!!!!!!");
        return geminiScores;
    }
}
