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
@Table(name = "app_user")

public class User {

    // personal
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "district")
    private String district;

    @Column(name = "state")
    private String state;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "age", nullable = false)
    private int age;

    // professional
    @Column(name = "experience")
    @ColumnDefault("0")
    private double experience;

    @Column(name = "highest_qualification_rank")
    @ColumnDefault("0")
    private int highestQualificationRank;

    @Column(name = "stream")
    private String stream;

    @Column(name = "specialization")
    private String specialization;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<UserSkill> skills;

    @Column(name = "csv_ref_id", nullable = true)
    private int csvRefId;

}
