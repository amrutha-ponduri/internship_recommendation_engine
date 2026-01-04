package com.example.reccomendation_system.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "internshiprequirements")
public class InternshipRequirements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "internship_id")
    private Internship internship;

    @Column(name = "mode")
    private String mode;

    @Column(name = "minimum_qualification_rank")
    @ColumnDefault("0")
    private int minimumQualificationRank;

    @Column(name = "min_experience")
    private int minExperience;

    @Column(name = "max_experience")
    private int maxExperience;

    @Column(name = "stream")
    private String stream;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "internshipRequirements", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<InternshipSkill> skills;

    @Column(name = "csv_ref_id", nullable = true)
    private int csvRefId;

}
