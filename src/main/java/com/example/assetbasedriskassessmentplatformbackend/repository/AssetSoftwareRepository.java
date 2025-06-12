package com.example.assetbasedriskassessmentplatformbackend.repository;

import com.example.assetbasedriskassessmentplatformbackend.entity.AssetsBasicInfo;
import com.example.assetbasedriskassessmentplatformbackend.entity.AssetsSoftware;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetSoftwareRepository extends JpaRepository<AssetsSoftware, Long> {
}
