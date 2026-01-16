package com.example.reccomendation_system.repository;

import com.example.reccomendation_system.dto.LocationDTO;
import com.example.reccomendation_system.model.Internship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface InternshipJpaRepository extends JpaRepository<Internship, Integer> {

    @Query("""
            SELECT
            MAX(
                CASE i.totalCount
                    WHEN 0 THEN NULL
                    ELSE (i.appliedCount * 1.0 / i.totalCount)
                    END
                ),
            MIN(
                CASE i.totalCount
                    WHEN 0 THEN NULL
                    ELSE (i.appliedCount * 1.0 / i.totalCount)
                    END
                ),
            MAX(i.totalCount),
            MIN(i.totalCount)
            FROM Internship i
            WHERE i.id IN :ids""")
    List<Object[]> findMaxMinRatiosAndTotalCounts(@Param("ids") List<Integer> eligibleInternshipIds);

    @Query("""
            SELECT i.id, i.totalCount,
            CASE i.totalCount
                WHEN 0 THEN NULL
                ELSE i.appliedCount * 1.0 / i.totalCount
            END
            FROM Internship i
            WHERE i.id IN :ids
            """)
    List<Object[]> findAllTotalCountsAndAppliedRatiosById(@Param("ids") List<Integer> eligibleInternshipIds);

    @Query("""
            SELECT new com.example.reccomendation_system.dto.LocationDTO(i.id, c.cityName,
            c.latitude, c.longitude, s.stateName, s.latitude, s.longitude) FROM Internship i
            LEFT JOIN StateCoordinates s ON s.stateName = i.state
            LEFT JOIN CityCoordinates c ON c.cityName = i.district WHERE i.id IN :ids""")
    List<LocationDTO> findAllLocationCoordinatesById(@Param("ids") List<Integer> eligibleInternshipIds);

    @Query("""
            SELECT MAX(timestampdiff(HOUR, i.postingTime, CURRENT_TIMESTAMP)),
            MIN(timestampdiff(HOUR, i.postingTime, CURRENT_TIMESTAMP))
            FROM Internship i
            WHERE i.id IN :ids""")
    List<Object[]> findMaxAndMinPostingTimeDifference(@Param("ids") List<Integer> eligibleInternshipIds);

    @Query("""
            SELECT
            i.id, timestampdiff(HOUR, i.postingTime, CURRENT_TIMESTAMP)
            FROM Internship i
            WHERE i.id IN :ids""")
    List<Object[]> findAllPostingTimeDifferenceById(@Param("ids") List<Integer> eligibleInternshipIds);

    @Query("""
            SELECT i.field, i.id FROM Internship i
            WHERE i.id IN :ids""")
    List<Object[]> findAllFieldsById(@Param("ids") List<Integer> eligibleInternshipIds);

}
