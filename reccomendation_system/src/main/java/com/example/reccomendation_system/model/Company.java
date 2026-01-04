package com.example.reccomendation_system.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable  = false)
    private int id;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "company_tier", nullable = false)
    private int companyTier;

    @Column(name = "csv_ref_id", nullable = true)
    private int csvRefId;

}
