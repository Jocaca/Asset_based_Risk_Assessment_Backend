package com.example.assetbasedriskassessmentplatformbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "evidence_chain" )
@Data
public class EvidenceChain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    //这里的asset是新的asset，是可以被用户所修改的
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset", referencedColumnName = "asset_id")
    private AssetsBasicInfo asset;

    @Column(name = "generate_date")
    private Date generateDate;

    //后续没有筛选的需求，直接存username
    @Column(name = "generate_by")
    private String generateBy;

    //这里开始存储的都是generate的时候asset的历史信息
    @Column(name="asset_name")
    private String assetName;

    @Column(name="asset_type")
    private String assetType;

    @Column(name = "asset_owner")
    private String assetOwner;

    @Column(name = "asset_status")
    private String assetStatus;

    //用pdf存储当前evidence的所有信息,这个字段保存pdf的url
    @Column(name = "snapshot")
    private String snapshot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audit_project", referencedColumnName = "id")
    private AuditProject auditProject;
}
