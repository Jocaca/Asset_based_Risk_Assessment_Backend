package com.example.assetbasedriskassessmentplatformbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.example.assetbasedriskassessmentplatformbackend.mapper",
        annotationClass = org.apache.ibatis.annotations.Mapper.class)
public class AssetBasedRiskAssessmentPlatformBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssetBasedRiskAssessmentPlatformBackendApplication.class, args);
    }

}
