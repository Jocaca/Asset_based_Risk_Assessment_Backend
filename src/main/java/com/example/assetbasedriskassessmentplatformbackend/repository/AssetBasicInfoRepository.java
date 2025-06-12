package com.example.assetbasedriskassessmentplatformbackend.repository;

import com.example.assetbasedriskassessmentplatformbackend.entity.AssetsBasicInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssetBasicInfoRepository extends JpaRepository<AssetsBasicInfo, Long>,
        JpaSpecificationExecutor<AssetsBasicInfo> {
    @Query("SELECT COUNT(a) FROM AssetsBasicInfo a WHERE " +
            "(:assetType = -1 OR a.assetType = :assetType) AND " +
            "(:emptyField = -1 OR a.emptyFields = :emptyField) AND " +
            "(:importance = -1 OR a.importance = :importance) AND " +
            "(:status = -1 OR a.status = :status)")
    long countWithFilters(
            @Param("assetType") Integer assetType,
            @Param("emptyField") Integer emptyField,
            @Param("importance") Integer importance,
            @Param("status") Integer status);

    @Query("SELECT COUNT(a) FROM AssetsBasicInfo a WHERE " +
            "LOWER(a.assetName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.assetOwner.assetUserName) LIKE LOWER(CONCAT('%', :search, '%'))")
    long countWithSearch(@Param("search") String search);

    @Query("SELECT a FROM AssetsBasicInfo a " +
            "WHERE (:assetType = -1 OR a.assetType = :assetType) " +
            "AND (:emptyField = -1 OR a.emptyFields = :emptyField) " +
            "AND (:importance = -1 OR a.importance = :importance) " +
            "AND (:status = -1 OR a.status = :status)")
    List<AssetsBasicInfo> findFilteredAssets(
            @Param("assetType") int assetType,
            @Param("emptyField") int emptyField,
            @Param("importance") int importance,
            @Param("status") int status,
            Pageable pageable); // 改用 Pageable 分页

    @Query("SELECT a FROM AssetsBasicInfo a WHERE " +
            "LOWER(a.assetName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.assetOwner.assetUserName) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<AssetsBasicInfo> findSearchAssets(
            @Param("search") String search,
            Pageable pageable); // 改用 Pageable 分页
}
