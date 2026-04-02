package com.example.reccomendation_system.dto.gemini_dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GeminiOutputDTO {
    private Integer internshipId;
    private Double score;
}
