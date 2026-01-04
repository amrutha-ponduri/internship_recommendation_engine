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

    @JsonProperty("user_age")
    private int age;
    @JsonProperty("user_gender")
    private String gender;
    @JsonProperty("user_experience")
    private double experience;
    @JsonProperty("user_stream")
    private String stream;
    @JsonProperty("user_specialization")
    private String specialization;
    @JsonProperty("user_qualification")
    private  int qualification;
    @JsonProperty("user_district")
    private String userDistrict;
    @JsonProperty("user_state")
    private String userState;
    @JsonProperty("user_skills")
    private ArrayList<String> skills;
}
