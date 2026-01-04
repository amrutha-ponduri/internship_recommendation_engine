package com.example.reccomendation_system.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "internship_skill")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternshipSkill {

    @EmbeddedId
    private InternshipSkillId internshipSkillId = new InternshipSkillId();

    @ManyToOne
    @MapsId("internshipId")
    @JoinColumn(name = "internship_id")
    private InternshipRequirements internshipRequirements;

    @ManyToOne
    @MapsId("skillId")
    @JoinColumn(name = "skill_id")
    private Skill skill;
}
