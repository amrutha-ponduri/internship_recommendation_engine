package com.example.reccomendation_system.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "internship")
public class Internship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title", nullable = false)
    private String title; // NOT NULL

    @ManyToOne
    @JoinColumn(name = "companyid", nullable = false)
    private Company company;// FK , NOT NULL

    @Column(name = "minstipend")
    @ColumnDefault("0")
    private double minStipend;

    @Column(name = "maxstipend")
    @ColumnDefault("0")
    private double maxStipend;

    @Column(name = "duration")
    private String duration; // 1 month

    @Column(name = "postingtime", nullable = false)
    private Date postingTime; // not null

    @Column(name = "sector")
    private String sector;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "appliedcount")
    @ColumnDefault("0")
    private int appliedCount;

    @Column(name = "totalcount")
    @ColumnDefault("0")
    private int totalCount;

    @Column(name = "benefits")
    private String benefits;

    @Column(name = "state")
    private String state;

    @Column(name = "district")
    private String district;

    @Column(name = "field")
    private String field;

    @Column(name = "csv_ref_id", nullable = true)
    private int csvRefId;

    // reverse join
    @OneToOne(mappedBy = "internship")
    private InternshipRequirements internshipRequirements;
}
