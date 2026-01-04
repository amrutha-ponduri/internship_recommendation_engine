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
    @JsonProperty("internship_benefits")
    private String benefits;
    @JsonProperty("internship_max_stipend")
    private double maxStipend;
    @JsonProperty("internship_min_stipend")
    private double minStipend;
    @JsonProperty("internship_field")
    private String field;
    @JsonProperty("internship_sector")
    private String sector;
    @JsonProperty("total_count")
    private int totalCount;
    @JsonProperty("internship_required_skills")
    public ArrayList<String> internshipReqSkills;
    @JsonProperty("internship_min_experience")
    private double minExperience;
    @JsonProperty("internship_max_experience")
    private double maxExperience;
    @JsonProperty("company")
    private String company;
    @JsonProperty("internship_min_qualification")
    private int minQualification;
}
