package com.example.assetbasedriskassessmentplatformbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "risk_relationship")
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public class RiskRelationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rid")
    private Integer RID;

    @ManyToOne
    @JoinColumn(name = "asset", referencedColumnName = "asset_id")
    private AssetsBasicInfo Asset;

    @ManyToOne
    @JoinColumn(name = "risk_type", referencedColumnName = "typeid")
    private RiskType riskType;

    @ManyToOne
    @JoinColumn(name = "risk_treatment", referencedColumnName = "id")
    private RiskTreatment riskTreatment;

    @ManyToOne
    @JoinColumn(name = "risk_owner", referencedColumnName = "assetUserId")
    private User riskOwner;

    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "applicable")
    private Boolean applicable;

    @Column(name="comments")
    private String comments;
}
