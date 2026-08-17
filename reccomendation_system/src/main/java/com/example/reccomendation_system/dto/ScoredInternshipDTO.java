package com.example.reccomendation_system.dto;

import com.example.reccomendation_system.model.Company;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoredInternshipDTO {

    private int internshipId;
    private String title;
    private Company company;
    private String duration;
    private Date postingTime;
    private String sector;
    private String description;
    private int appliedCount;
    private int totalCount;
    private String benifits;
    private String state;
    private String district;
    private double score;
}
