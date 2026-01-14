package com.example.reccomendation_system.dto.ml_model_dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InternshipInfoDTO {

    @JsonProperty("internship_id")
    public String internshipId;
    @JsonProperty("internship_field")
    private String field;
    @JsonProperty("internship_sector")
    private String sector;
    @JsonProperty("internship_required_skills")
    public ArrayList<String> internshipReqSkills;
    @JsonProperty("internship_min_experience")
    private double minExperience;
    @JsonProperty("internship_max_experience")
    private double maxExperience;
}
