package com.example.assetbasedriskassessmentplatformbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "assets_physical")
@Data
@PrimaryKeyJoinColumn(name = "asset_id")
public class AssetsPhysical extends AssetsBasicInfo {
    @Column(name = "fixed_physical_asset", nullable = false)
    private Integer fixedPhysicalAsset; // 0=Yes,1=No

    @Column(name = "Asset_category")
    private Integer assetCategory; // 0=Buildings & Structures, 1=Production Equipment & Machinery, etc.

    @Column(name = "location")
    private String location;

    @Column(name = "Asset_category2")
    private Integer assetCategory2; // 0=Mobile Electronic Devices, etc.

    @Column(name = "Current_holder")
    private String currentHolder;

    @Column(name = "Checkout_date")
    private Date checkoutDate;

    @Column(name = "Expected_return_date")
    private Date expectedReturnDate;

    @Column(name = "Conditions")
    private Integer conditions; // 0=Damaged,1=Good

    @Column(name = "Date_encryption")
    private Integer dateEncryption; // 0=False,1=True

    @Column(name = "Remote_wipe_capability")
    private Integer remoteWipeCapability; // 0=False,1=True

    @Column(name = "Epurchase_date")
    private Date epurchaseDate;

    @Column(name = "Depreciation_period")
    private Integer depreciationPeriod;

    @Column(name = "Maintenance_cycle")
    private Integer maintenanceCycle; // 0=Daily, Weekly, etc.
}