package com.example.assetbasedriskassessmentplatformbackend.entity;
import jakarta.persistence.*;
import lombok.Data;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "assets_basic_info")
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public class AssetsBasicInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asset_id")
    private Integer assetId;

    @Column(name = "asset_name", nullable = false)
    private String assetName;

    @Column(name = "asset_type")
    private Integer assetType; // 0=software,1=physical,2=information,3=people

    @ManyToOne
    @JoinColumn(name = "asset_owner", referencedColumnName = "assetUserId")
    private User assetOwner;

    @Column(name = "contact")
    private String contact;

    @Column(name = "description")
    private String description;

    @Column(name = "SWID")
    private String swid;

    @Column(name = "status")
    private Integer status; // 0=Active,1=Decommissioned

    @Column(name = "improtance")
    private Integer importance; // 0=Low,1=Medium,2=High,3=Extremely High

    @Column(name = "Associated_assets")
    private String associatedAssets;

    @Column(name = "QStatus", nullable = false)
    private Integer qStatus; // 0=in_progress, 1=finished

    @Column(name = "empty_field")
    private Integer emptyFields; //0=No,1=Yes
//    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

//    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;
}