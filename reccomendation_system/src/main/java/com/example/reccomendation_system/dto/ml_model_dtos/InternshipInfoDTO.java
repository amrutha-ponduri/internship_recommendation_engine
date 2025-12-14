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
    @JsonProperty("required_skills")
    public ArrayList<String> internshipReqSkills;
    @JsonProperty("internship_location")
    public String internshipLocation;

    public String company;
    public String sector;
    public String role;

}
