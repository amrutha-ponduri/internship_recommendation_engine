package com.example.reccomendation_system.repository;

import com.example.reccomendation_system.dto.gemini_dtos.InternshipDetailsGeminiQueryDTO;
import com.example.reccomendation_system.model.InternshipRequirements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface InternshipRequirementsJpaRepository extends JpaRepository<InternshipRequirements, Integer> {

    @Query("""
            SELECT ir.internship.id
            FROM InternshipRequirements ir
            WHERE ir.minimumQualificationRank <= :userQualificationRank
            """)
    List<Integer> findAllEligibleInternshipIds(int userQualificationRank);

    @Query("""
            SELECT ir.internship.id, ir.mode
            FROM InternshipRequirements ir
            WHERE ir.internship.id IN :ids
            """)
    List<Object[]> findAllModesById(@Param("ids") List<Integer> eligibleInternshipIds);

    @Query("""
            SELECT new com.example.reccomendation_system.dto.gemini_dtos.InternshipDetailsGeminiQueryDTO(
            ir.internship.id, ir.internship.company.companyName, ir.internship.title, s.skill.skillName)
            FROM InternshipRequirements ir JOIN ir.skills s WHERE ir.internship.id IN :ids""")
    List<InternshipDetailsGeminiQueryDTO> findAllInternshipDetailsForGemini(ArrayList<Integer> ids);
}
