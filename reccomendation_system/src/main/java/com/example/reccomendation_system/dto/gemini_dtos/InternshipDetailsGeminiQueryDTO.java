package com.example.reccomendation_system.dto.gemini_dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InternshipDetailsGeminiQueryDTO {
    private Integer internshipId;
    private String companyName;
    private String internshipRole;
    private String skillName;
}
