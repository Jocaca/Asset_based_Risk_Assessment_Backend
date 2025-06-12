package com.example.assetbasedriskassessmentplatformbackend.repository;

import com.example.assetbasedriskassessmentplatformbackend.entity.AssetsBasicInfo;
import com.example.assetbasedriskassessmentplatformbackend.entity.AssetsPeople;
import com.example.assetbasedriskassessmentplatformbackend.entity.AssetsPhysical;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetPeopleRepository extends JpaRepository<AssetsPeople, Long> {
}
