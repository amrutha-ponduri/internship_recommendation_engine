package com.example.reccomendation_system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequirements {

//    private String role;
//    private String sector;
    @JsonProperty("min_stipend")
    private double minStipend;
    @JsonProperty("preferred_mode")
    private String preferredMode;
    @JsonProperty("preferred_city")
    private String preferredCity;
    @JsonProperty("preferred_state")
    private String preferredState;
//    @JsonProperty("employment_type")
//    private String employmentType;
    @JsonProperty("preferred_domain")
    private String preferredDomain;

}
