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
public class ProjectExperienceDescription {
    @JsonProperty("project_description")
    private String projectDescription;
    @JsonProperty("experience_description")
    private String experienceDescription;
}
