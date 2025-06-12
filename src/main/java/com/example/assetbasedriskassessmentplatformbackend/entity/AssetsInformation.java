package com.example.assetbasedriskassessmentplatformbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "assets_information")
@Data
@PrimaryKeyJoinColumn(name = "asset_id")
public class AssetsInformation extends AssetsBasicInfo {
    @Column(name = "asset_category", nullable = false)
    private Integer assetCategory; // 0=Database, 1=Document, 2=Patent

    @Column(name = "Retention_policy")
    private Integer retentionPolicy; // 0=Permanent, 1=5Years

    @Column(name = "Storage_location")
    private String storageLocation;

    @Column(name = "data_schema")
    private Integer dataSchema; // 0=MYSQL, 1=MONGODB

    @Column(name = "asset_version")
    private String assetVersion;

    @Column(name = "Contains_PII")
    private Integer containsPII; // 0=false,1=true

    @Column(name = "Backup_frequency")
    private Integer backupFrequency; // 0=daily, 1=weekly, 2=monthly

    @Column(name = "File_format")
    private Integer fileFormat; // 0=pdf,1=docx,2=cad

    @Column(name = "confidentiality_level")
    private Integer confidentialityLevel; // 0=public, 1=internal, 2=sec

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "expiry_date")
    private Date expiryDate;
}