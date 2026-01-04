package com.example.reccomendation_system.repository;

import com.example.reccomendation_system.model.InternshipRequirements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternshipRequirementsJpaRepository extends JpaRepository<InternshipRequirements, Integer> {
    @Query("""
       SELECT ir.internship.id
       FROM InternshipRequirements ir
       WHERE ir.minimumQualificationRank <= :userQualificationRank
       """)
    List<Integer> findAllEligibleInternshipIds(int age, String gender, int userQualificationRank);

    @Query("""
            SELECT ir.mode FROM InternshipRequirements ir JOIN ir.internship i
            WHERE i.id = :internshipId
            """)
    String findModeByInternshipId(int internshipId);

    @Query("""
            SELECT ir.internship.id, ir.mode
            FROM InternshipRequirements ir
            WHERE ir.internship.id IN :ids
            """)
    List<Object[]> findAllModesById(@Param("ids") List<Integer> eligibleInternshipIds);
}
