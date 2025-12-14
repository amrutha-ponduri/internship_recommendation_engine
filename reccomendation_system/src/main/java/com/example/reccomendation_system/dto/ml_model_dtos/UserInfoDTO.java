package com.example.reccomendation_system.dto.ml_model_dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {

    private int age;
    private String gender;

    @JsonProperty("user_skills")
    private ArrayList<String> skills;

    @JsonProperty("user_location")
    private String user_location;

    @JsonProperty("graduation_year")
    private int graduationYear;

    private double gpa;
    private double experience;

}
