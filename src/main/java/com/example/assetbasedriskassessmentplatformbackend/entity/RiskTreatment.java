package com.example.assetbasedriskassessmentplatformbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "risk_treatment")
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public class RiskTreatment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer ID;

    @Column(name = "risk_level")
    private Integer riskLevel; // 0=Very Low, 1=Low, 2=Medium, 3=High, 4=Very high

    @Column(name = "treatment_option")
    private Integer treatmentOption; // 0=Risk Avoidance, 1=Risk Modification, 2=Risk Retention, 3=Risk Sharing

    @OneToMany(mappedBy = "riskTreatment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Files> evidenceFiles = new ArrayList<>();

    @Column(name="comments")
    private String comments;

}
