package com.example.assetbasedriskassessmentplatformbackend.repository;

import com.example.assetbasedriskassessmentplatformbackend.entity.AssetsBasicInfo;
import com.example.assetbasedriskassessmentplatformbackend.entity.AssetsInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetInformationRepository extends JpaRepository<AssetsInformation, Long> {
}
