package com.example.reccomendation_system.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "skill_name", nullable = false, unique = true) // NOT NULL UNIQUE
    private String skillName;

    @Column(name = "csv_ref_id", nullable = true)
    private int csvRefId;

}
