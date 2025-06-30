package com.example.assetbasedriskassessmentplatformbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
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

    @OneToOne
    @JoinColumn(name = "risk_relationship", referencedColumnName = "rid") // 仍然使用现有的外键列
    private RiskRelationship riskRelationship;

    @Column(name="valid")
    private Integer valid;
    //用于记录当前记录是暂存记录（0）还是有效记录（1）。
    //只有有效记录生效，暂存记录仅作保存使用。暂存记录可以修改，有效记录不可以。log功能只显示有效记录
    //在my-risk详情页中，当用户点击save按钮，该条记录视为暂存记录，点击done按钮，该记录视为有效记录（当记录设置为有效记录的时候，需要同步修改update_date以及relationship表中的treatmentStatus=finish）。
    //用户只可以done一次。如果当前已经有一条done的记录了，用户点击编辑页面的时候，save和done按钮应该都在禁用状态。
    // 存在risk owner在修改treatment的同时asset owner重新分配了riskowner.因此提交记录的时候需要先检测当前这个risk owner是否有效（检查risk relationship中的valid字段）。如果已经失效，则不做提交保存，向前端返回失效信息

    @Column(name="update_date")
    private Date updateDate; //只记录该record生效的时间。只有当valid设置为1的时候同时修改。
}
