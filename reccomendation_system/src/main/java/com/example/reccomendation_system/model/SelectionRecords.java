package com.example.reccomendation_system.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "selection_records")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SelectionRecords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "max_exp_gap")
    private double maxExpGap;
    @Column(name = "min_exp_gap")
    private double minExpGap;
    @Column(name = "is_exp_in_range")
    private int isExpInRange;
    @Column(name = "user_experience")
    private double userExperience;
    @Column(name = "user_skills")
    @Lob
    private String userSkills;
    @Column(name = "internship_field")
    private String internshipField;
    @Column(name = "internship_sector")
    private String internshipSector;
    @Column(name = "internship_required_skills")
    @Lob
    private String internshipRequiredSkills;
    @Column(name = "selection_status")
    private int selectionStatus;
    @Column(name = "csv_ref_id")
    private int csvRefId;
}
