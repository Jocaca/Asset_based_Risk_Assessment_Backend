package com.example.assetbasedriskassessmentplatformbackend.repository;
import com.example.assetbasedriskassessmentplatformbackend.entity.AssetsBasicInfo;
import com.example.assetbasedriskassessmentplatformbackend.entity.RiskRelationship;
import com.example.assetbasedriskassessmentplatformbackend.entity.RiskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface RiskRelationshipRepository extends JpaRepository<RiskRelationship, Integer> {

    @Query("SELECT COUNT(r) FROM RiskRelationship r WHERE " +
            "(:assetId = -1 OR r.Asset.assetId = :assetId)")
    long countByAsset(@Param("assetId") Integer assetId);

    @Query("SELECT r.riskType.typeID FROM RiskRelationship r WHERE " +
            "(:assetId = -1 OR r.Asset.assetId = :assetId)")
    List<Integer> findRiskTypeIdsByAsset(@Param("assetId") Integer assetId);

    @Query("SELECT r FROM RiskRelationship r WHERE " +
            "(:assetId = -1 OR r.Asset.assetId = :assetId)")
    List<RiskRelationship> findByAsset(@Param("assetId") Integer assetId);

    @Query("SELECT DISTINCT r FROM RiskRelationship r " +
            "WHERE r.Asset.assetId = :assetId AND r.riskType.typeID = :typeId")
    List<RiskRelationship> findByAssetIdAndRiskType(
            @Param("assetId") Integer assetId,
            @Param("typeId") Integer typeID);

    @Query("SELECT r FROM RiskRelationship r WHERE r.Asset.assetId = :assetId AND r.riskType.typeID = :typeId AND r.valid = :valid")
    Optional<RiskRelationship> findByAssetIdAndRiskTypeAndValid(
            @Param("assetId") Integer assetId,
            @Param("typeId") Integer typeID,
            @Param("valid") Integer valid);

    @Query("SELECT r FROM RiskRelationship r WHERE r.Asset.assetId = :assetId AND r.riskType.typeID = :typeId AND r.valid IN :validStatuses ORDER BY r.createDate DESC")
    List<RiskRelationship> findByAssetIdAndRiskTypeAndValidIn(
            @Param("assetId") Integer assetId,
            @Param("typeId") Integer typeID,
            @Param("validStatuses") List<Integer> validStatuses);

    @Query("SELECT r FROM RiskRelationship r WHERE r.Asset.assetId = :assetId AND r.valid = :valid")
    List<RiskRelationship> findByAssetIdAndValid(
            @Param("assetId") Integer assetId,
            @Param("valid") Integer valid);

}
