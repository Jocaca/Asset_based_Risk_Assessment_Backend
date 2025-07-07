package com.example.assetbasedriskassessmentplatformbackend.repository;

import com.example.assetbasedriskassessmentplatformbackend.entity.EvidenceChain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvidenceChainRepository extends JpaRepository<EvidenceChain,Integer> {
    // 方法1：使用关联属性查询
    List<EvidenceChain> findByAsset_AssetId(Integer assetId);

    // 方法2：更清晰的写法（推荐）
    @Query("SELECT ec FROM EvidenceChain ec WHERE ec.asset.assetId = :assetId")
    List<EvidenceChain> findByAssetId(@Param("assetId") Integer assetId);

    // 计数方法
    Long countByAsset_AssetId(Integer assetId);

    // 或者使用JPQL明确指定
    @Query("SELECT COUNT(ec) FROM EvidenceChain ec WHERE ec.asset.assetId = :assetId")
    Long countByAssetId(@Param("assetId") Integer assetId);
}
