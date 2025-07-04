package com.example.assetbasedriskassessmentplatformbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



@Entity
@Data
@Table(name = "assets_questionaire")
public class assets_questionaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Q1")
    private String Q1;

    @Column(name = "Q2")
    private String Q2;

    @Column(name = "Q3")
    private String Q3;

    @Column(name = "Q4")
    private String Q4;

    @Column(name = "Q5")
    private String Q5;

    @Column(name = "Q6")
    private String Q6;

    @Column(name = "Q7")
    private String Q7;

    @Column(name = "Q8")
    private String Q8;

    @Column(name = "Q9")
    private String Q9;

    @Column(name = "Q10")
    private String Q10;

    @Column(name = "Q11")
    private String Q11;

    @Column(name = "Q12")
    private String Q12;

    @Column(name = "Q13")
    private String Q13;

    @Column(name = "Q14")
    private String Q14;

    @Column(name = "Q15")
    private String Q15;

    @Column(name = "Q16")
    private String Q16;

    @Column(name = "Q17")
    private String Q17;

    @Column(name = "Q18")
    private String Q18;

    @Column(name = "Q19")
    private String Q19;

    @Column(name = "Q20")
    private String Q20;

    @Column(name = "Q21")
    private String Q21;

    @Column(name = "Q22")
    private String Q22;

    @Column(name = "Q23")
    private String Q23;

    @Column(name = "Q24")
    private String Q24;

    @Column(name = "Q25")
    private String Q25;

    @Column(name = "risks")
    private String risks;

    @Column(name = "warning")
    private String warning;

    // Getters and Setters
}
