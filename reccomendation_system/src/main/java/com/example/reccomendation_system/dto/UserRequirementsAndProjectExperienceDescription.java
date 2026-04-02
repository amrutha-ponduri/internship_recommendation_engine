package com.example.reccomendation_system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequirementsAndProjectExperienceDescription {
    @JsonProperty("user_requirements")
    private UserRequirements userRequirements;
    @JsonProperty("project_experience_description")
    private ProjectExperienceDescription projectExperienceDescription;
}
