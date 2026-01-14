package com.example.reccomendation_system.util;

import com.example.reccomendation_system.dto.InternshipDTO;
import com.example.reccomendation_system.mapper.Mapper;
import com.example.reccomendation_system.model.Internship;
import com.example.reccomendation_system.model.User;
import com.example.reccomendation_system.repository.InternshipJpaRepository;
import com.example.reccomendation_system.repository.InternshipRequirementsJpaRepository;
import com.example.reccomendation_system.repository.UserJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class EligibilityFiltering {

    private final EntityManager entityManager;
    private final UserJpaRepository userJpaRepository;
    private final InternshipJpaRepository internshipJpaRepository;
    private final InternshipRequirementsJpaRepository internshipRequirementsJpaRepository;
    private final Mapper mapper;
    final int minimumCount = 3;

    @Autowired
    public EligibilityFiltering(EntityManager entityManager, UserJpaRepository userJpaRepository, InternshipJpaRepository internshipJpaRepository, InternshipRequirementsJpaRepository internshipRequirementsJpaRepository, Mapper mapper) {
        this.entityManager = entityManager;
        this.userJpaRepository = userJpaRepository;
        this.internshipJpaRepository = internshipJpaRepository;
        this.internshipRequirementsJpaRepository = internshipRequirementsJpaRepository;
        this.mapper = mapper;
    }
    public ArrayList<InternshipDTO> getEligibleInternships(int userId) {
        ArrayList<Integer> internshipIdsList = getEligibleInternshipIds(userId);
        ArrayList<InternshipDTO> internshipDTOs = new ArrayList<>();
        for (int internshipId : internshipIdsList) {
            Internship internship = internshipJpaRepository.findById(internshipId).get();
            InternshipDTO internshipDTO = mapper.toInternshipDTO(internship);
            internshipDTOs.add(internshipDTO);
        }
        return internshipDTOs;
    }

    public ArrayList<Integer> getEligibleInternshipIds(int userId) {
        try {
            User user = userJpaRepository.findById(userId).get();
            int userAge = user.getAge();
            String userGender = user.getGender();
            int highestQualificationRank = user.getHighestQualificationRank();
            double threshold = 0.3;
            String queryStatement = """
                               SELECT ir.internship_id
                        FROM internshiprequirements ir
                        JOIN internship_skill i_s
                            ON ir.internship_id = i_s.internship_id
                        LEFT JOIN user_skill u_s
                            ON i_s.skill_id = u_s.skill_id
                            AND u_s.user_id = :userId
                        WHERE
                            ir.minimum_qualification_rank <= :highestQualificationRank
                        GROUP BY ir.internship_id
                        HAVING
                            COUNT(u_s.skill_id) * 1.0 / COUNT(i_s.skill_id) >= :threshold;
                        """;
                Query nativeQuery = entityManager.createNativeQuery(queryStatement, Integer.class);
                nativeQuery.setParameter("highestQualificationRank", highestQualificationRank);
                nativeQuery.setParameter("userId", userId);
                nativeQuery.setParameter("threshold", threshold);
                List<Integer> eligibleInternshipIds = nativeQuery.getResultList();
                if (eligibleInternshipIds.size() < minimumCount) {
                    return new ArrayList<Integer>(internshipRequirementsJpaRepository.findAllEligibleInternshipIds(userAge, userGender, highestQualificationRank));
                }
                return new ArrayList<>(eligibleInternshipIds);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
