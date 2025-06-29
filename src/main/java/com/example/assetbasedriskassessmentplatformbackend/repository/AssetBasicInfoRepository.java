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
            "(:assetType = -1 OR a.assetType = :assetType) AND " +
            "(:status = -1 OR a.status = :status) AND " +
            "(:qstatus = -1 OR a.qStatus = :qstatus)")
    long countWithFilters_2(
            @Param("assetType") Integer assetType,
            @Param("status") Integer status,
            @Param("qstatus") Integer qstatus);

    @Query("SELECT COUNT(a) FROM AssetsBasicInfo a WHERE " +
            "(:assetType = -1 OR a.assetType = :assetType) AND " +
            "(:status = -1 OR a.status = :status) AND " +
            "(:rtstatus = -1 OR a.rtStatus = :rtstatus) AND " +
            "(a.assetOwner.assetUserId = :userId )")
    long countWithFilters_3(
            @Param("assetType") Integer assetType,
            @Param("status") Integer status,
            @Param("rtstatus") Integer rtstatus,
            @Param("userId") int userId);

     @Query("SELECT COUNT(a) FROM AssetsBasicInfo a WHERE " +
             "LOWER(a.assetName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
             "LOWER(a.assetOwner.assetUserName) LIKE LOWER(CONCAT('%', :search, '%'))")
     long countWithSearch(@Param("search") String search);

    @Query("SELECT COUNT(a) FROM AssetsBasicInfo a WHERE " +
            "LOWER(a.assetName) LIKE LOWER(CONCAT('%', :search, '%')) AND " +
            "(a.assetOwner.assetUserId = :userId)")
    long countWithSearch_2(@Param("search") String search,
                           @Param("userId") int userId);

    @Query("SELECT a FROM AssetsBasicInfo a WHERE " +
            "a.assetOwner.assetUserId = :userId ")
    Page<AssetsBasicInfo> findbyOnwer(
            @Param("userId") int userId,
            Pageable pageable);

    @Query("SELECT COUNT(a) FROM AssetsBasicInfo a WHERE " +
            "a.assetOwner.assetUserId = :userId")
    long countWithOwner(@Param("userId") int userId);

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
            Pageable pageable);

    @Query("SELECT a FROM AssetsBasicInfo a WHERE " +
            "LOWER(a.assetName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.assetOwner.assetUserName) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<AssetsBasicInfo> findSearchAssets(
            @Param("search") String search,
            Pageable pageable);

    @Query("SELECT a FROM AssetsBasicInfo a WHERE " +
            "LOWER(a.assetName) LIKE LOWER(CONCAT('%', :search, '%')) AND " +
            "(a.assetOwner.assetUserId = :userId)")
    List<AssetsBasicInfo> findSearchAssetsByOwner(
            @Param("search") String search,
            @Param("userId") int userId,
            Pageable pageable);

    @Query("SELECT a FROM AssetsBasicInfo a " +
            "WHERE (:assetType = -1 OR a.assetType = :assetType) " +
            "AND (:qstatus = -1 OR a.qStatus = :qstatus) " +
            "AND (:status = -1 OR a.status = :status)")
    List<AssetsBasicInfo> findFilteredAssets_2(
            @Param("assetType") Integer assetType,
            @Param("status") Integer status,
            @Param("qstatus") Integer qstatus,
            Pageable pageable);

    @Query("SELECT a FROM AssetsBasicInfo a " +
            "WHERE (:assetType = -1 OR a.assetType = :assetType) " +
            "AND (:rtstatus = -1 OR a.rtStatus = :rtstatus) " +
            "AND (:status = -1 OR a.status = :status)" +
            "AND (a.assetOwner.assetUserId = :userid)")
    List<AssetsBasicInfo> findFilteredAssets_3(
            @Param("assetType") Integer assetType,
            @Param("status") Integer status,
            @Param("rtstatus") Integer rtstatus,
            @Param("userid") int userid,
            Pageable pageable);
}
