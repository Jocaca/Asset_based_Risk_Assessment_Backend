package com.example.assetbasedriskassessmentplatformbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "audit_project" )
@Data
public class AuditProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "created_date")
    private Date createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "assetUserId")
    private User created_by;

    @Column(name = "status")
    private Integer status; //0=in-progress, 1=finished

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auditor", referencedColumnName = "assetUserId")
    private User Auditor;

    @OneToMany(mappedBy = "auditProject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EvidenceChain> assets = new ArrayList<>();

}
