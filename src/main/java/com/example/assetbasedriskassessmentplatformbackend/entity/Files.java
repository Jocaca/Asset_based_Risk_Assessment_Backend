package com.example.assetbasedriskassessmentplatformbackend.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "files")
@Data
public class Files {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_id")
    private RiskTreatment riskTreatment;

    @Column(nullable = false)
    private String originalName;  // 原始文件名

    @Column(nullable = false)
    private String storedName;    // 存储文件名(避免冲突)

    @Column(nullable = false)
    private String filePath;      // 文件存储路径或URL

    @Column(nullable = false)
    private String fileType;      // MIME类型

}
