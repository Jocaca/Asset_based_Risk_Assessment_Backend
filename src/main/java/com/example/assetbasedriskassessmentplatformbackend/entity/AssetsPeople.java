package com.example.assetbasedriskassessmentplatformbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "assets_people")
@Data
@PrimaryKeyJoinColumn(name = "asset_id")
public class AssetsPeople extends AssetsBasicInfo {
    @Column(name = "department")
    private String department;

    @Column(name = "position")
    private String position;

    @Column(name = "Hire_date")
    private Date hireDate;

    @Column(name = "background_check_status")
    private Integer backgroundCheckStatus; // 0=Completed, 1=Pending, etc.

    @Column(name = "security_training_status")
    private Integer securityTrainingStatus; // 0=Completed, 1=Pending, etc.

    @Column(name = "NDA_signing_date")
    private Date ndaSigningDate;

    @Column(name = "remote_work_agreement_status")
    private Integer remoteWorkAgreementStatus; // 0=false, 1=true

    @Column(name = "security_incident_records")
    private String securityIncidentRecords;

    @Column(name = "last_audit_date")
    private Date lastAuditDate;
}
