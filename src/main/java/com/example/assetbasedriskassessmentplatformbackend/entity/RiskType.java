package com.example.assetbasedriskassessmentplatformbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "risk_type" )
@Data
public class RiskType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "typeid")
    private Integer typeID;

    @Column(name = "content")
    private String Content;
}
