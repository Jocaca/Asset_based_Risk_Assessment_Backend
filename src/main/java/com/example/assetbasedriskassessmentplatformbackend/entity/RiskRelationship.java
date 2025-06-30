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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset", referencedColumnName = "asset_id")
    private AssetsBasicInfo Asset;

    @ManyToOne
    @JoinColumn(name = "risk_type", referencedColumnName = "typeid")
    private RiskType riskType;

    @ManyToOne
    @JoinColumn(name = "risk_owner", referencedColumnName = "assetUserId")
    private User riskOwner;

    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "applicable")
    private Boolean applicable;

    @Column(name="comments")
    private String comments;

    @Column(name="treatment_status")
    private String treatmentStatus; // 0=in_progress, 1=finished

    @OneToOne(mappedBy = "riskRelationship", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RiskTreatment riskTreatment; // 新增字段，不存储外键

    @Column(name="valid")
    private Integer valid;
    //用于记录当前记录是历史记录（0），暂存记录（1）还是有效记录（2）。
    //只有有效记录生效，历史记录和暂存记录仅作保存使用。暂存记录可以修改，历史记录和有效记录不可以。log功能只显示历史记录和有效记录

    @Column(name="create_date")
    private Date createDate; //只记录该record生效的时间。只有当valid设置为2的时候同时修改。
}
