package com.example.assetbasedriskassessmentplatformbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "asset_user_table")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assetUserId")
    private Integer assetUserId;

    @Column(name = "assetUserName", nullable = false, unique = true)
    private String assetUserName;

    @Column(name = "assetUserPwd", nullable = false)
    private String assetUserPwd;

    @Column(name = "assetUserEmail", unique = true)
    private String assetUserEmail;

    @Column(name = "assetUserLevel", columnDefinition = "TINYINT DEFAULT 3")
    private Integer assetUserLevel;

//    @CreationTimestamp
    @Column(name = "createdAt", updatable = false)
    private Date createdAt;

//    @UpdateTimestamp
    @Column(name = "updatedAt")
    private Date updatedAt;
}