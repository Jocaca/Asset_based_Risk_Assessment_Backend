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
            "(:userId = 0 OR a.assetOwner.assetUserId = :userId )")
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

    @Query("SELECT COUNT(a) FROM AssetsBasicInfo a JOIN a.risks r WHERE " +
            "(LOWER(a.assetName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.assetOwner.assetUserName) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "r.riskOwner.assetUserId = :userId AND " +
            "r.valid = 1")
    long countWithSearch_3(@Param("search") String search,
                           @Param("userId") int userId);

    @Query("SELECT a FROM AssetsBasicInfo a WHERE " +
            ":userId = 0 OR a.assetOwner.assetUserId = :userId ")
    Page<AssetsBasicInfo> findbyOnwer(
            @Param("userId") int userId,
            Pageable pageable);

    @Query("SELECT COUNT(a) FROM AssetsBasicInfo a WHERE " +
            ":userId = 0 OR a.assetOwner.assetUserId = :userId")
    long countWithOwner(@Param("userId") int userId);

    @Query("SELECT COUNT(a) FROM AssetsBasicInfo a " +
            "JOIN a.risks r " +
            "WHERE r.riskOwner.assetUserId = :userId " +
            "AND r.valid = 1")
    long countWithValidRisksByOwner(@Param("userId") int userId);

    @Query("SELECT COUNT(a) FROM AssetsBasicInfo a " +
            "JOIN a.risks r WHERE " +
            "(:assetType = -1 OR a.assetType = :assetType) AND " +
            "(:status = -1 OR a.status = :status) AND " +
            "(:treatmentstatus = -1 OR r.treatmentStatus = :treatmentstatus) AND " +
            "r.riskOwner.assetUserId = :userId AND " +
            "r.valid = 1")
    long countWithFilters_4(
            @Param("assetType") Integer assetType,
            @Param("status") Integer status,
            @Param("treatmentstatus") Integer treatmentstatus,
            @Param("userId") int userId);

    @Query("SELECT a FROM AssetsBasicInfo a " +
            "JOIN a.risks r WHERE " +
            "(:assetType = -1 OR a.assetType = :assetType) AND " +
            "(:status = -1 OR a.status = :status) AND " +
            "(:treatmentstatus = -1 OR r.treatmentStatus = :treatmentstatus) AND " +
            "(r.riskOwner.assetUserId = :userid) AND " +
            "r.valid = 1")
    List<AssetsBasicInfo> findFilteredAssets_4(
            @Param("assetType") Integer assetType,
            @Param("status") Integer status,
            @Param("treatmentstatus") Integer treatmentstatus,
            @Param("userid") int userid,
            Pageable pageable);

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

    @Query("SELECT a FROM AssetsBasicInfo a JOIN a.risks r WHERE " +
            "(LOWER(a.assetName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.assetOwner.assetUserName) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "r.riskOwner.assetUserId = :userId AND " +
            "r.valid = 1")
    List<AssetsBasicInfo> findSearchAssetsByOwner_2(
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
            "AND (:userid = 0 OR a.assetOwner.assetUserId = :userid)")
    List<AssetsBasicInfo> findFilteredAssets_3(
            @Param("assetType") Integer assetType,
            @Param("status") Integer status,
            @Param("rtstatus") Integer rtstatus,
            @Param("userid") int userid,
            Pageable pageable);

    @Query("SELECT a FROM AssetsBasicInfo a " +
            "JOIN a.risks r " +
            "WHERE r.riskOwner.assetUserId = :userId " +
            "AND r.valid = 1")
    Page<AssetsBasicInfo> findAssetsWithValidRisksByOwner(
            @Param("userId") Integer userId,
            Pageable pageable);

}
