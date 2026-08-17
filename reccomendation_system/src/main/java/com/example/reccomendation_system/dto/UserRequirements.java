package com.example.reccomendation_system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequirements {
    @JsonProperty("preferred_mode")
    private String preferredMode;
    @JsonProperty("preferred_state")
    private String preferredState;
    @JsonProperty("preferred_domain")
    private String preferredDomain;

}
