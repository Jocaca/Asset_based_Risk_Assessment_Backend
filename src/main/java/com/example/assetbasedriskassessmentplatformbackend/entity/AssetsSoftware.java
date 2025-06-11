package com.example.assetbasedriskassessmentplatformbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "assets_software")
@Data
@PrimaryKeyJoinColumn(name = "asset_id")
public class AssetsSoftware extends AssetsBasicInfo {
    @Column(name = "Version", nullable = false)
    private String version;

    @Column(name = "Install_date", nullable = false)
    private Date installDate;

    @Column(name = "Operating_Systems", nullable = false)
    private String operatingSystems;

    @Column(name = "External_Supplied_Service", nullable = false)
    private Integer externalSuppliedService; // 0=yes,1=no

    @Column(name = "Manufacture")
    private String manufacture;

    @Column(name = "service_type")
    private Integer serviceType; // 0=On-Premises,1=SaaS,2=Hybrid,3=Hosted

    @Column(name = "License_type")
    private Integer licenseType; // 0=Permanent,1=Subscription,2=Trial,3=volume licensing

    @Column(name = "License_Start_Date")
    private Date licenseStartDate;

    @Column(name = "License_End_Date")
    private Date licenseEndDate;

    @Column(name = "License_Number")
    private String licenseNumber;

    @Column(name = "Related_contract_number")
    private String relatedContractNumber;
}