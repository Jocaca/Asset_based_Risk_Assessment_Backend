package com.example.assetbasedriskassessmentplatformbackend.repository;

import com.example.assetbasedriskassessmentplatformbackend.entity.AuditProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditProjectHomeRepository extends JpaRepository<AuditProject, Integer> {

    // 获取所有项目（带用户权限过滤）
    @Query("SELECT a FROM AuditProject a WHERE " +
            "(:userLevel = 0 OR :userLevel = 1 AND a.Auditor.assetUserId = :userId)")
    Page<AuditProject> findAllWithPermission(
            @Param("userLevel") int userLevel,
            @Param("userId") int userId,
            Pageable pageable);

    // 获取过滤项目（带用户权限过滤）
    @Query("SELECT a FROM AuditProject a WHERE " +
            "a.status = :statusCode " +
            "AND (:userLevel = 1 AND a.Auditor.assetUserId = :userId)")
    Page<AuditProject> findFilteredProjectsWithPermissionAndStatus(
            @Param("statusCode") Integer statusCode,
            @Param("userLevel") int userLevel,
            @Param("userId") int userId,
            Pageable pageable);
    @Query("SELECT a FROM AuditProject a WHERE " +
            "(:userLevel = 1 AND a.Auditor.assetUserId = :userId)")
    Page<AuditProject> findFilteredProjectsWithPermission(
            @Param("userLevel") int userLevel,
            @Param("userId") int userId,
            Pageable pageable);

    @Query("SELECT a FROM AuditProject a WHERE " +
            "a.status = :statusCode ")
    Page<AuditProject> findFilteredProjects(
            @Param("statusCode") Integer statusCode,
            Pageable pageable);

    // 搜索项目（带用户权限过滤）
    @Query("SELECT a FROM AuditProject a WHERE " +
            "(LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.created_by.assetUserName) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:userLevel = 1 AND a.Auditor.assetUserId = :userId)")
    Page<AuditProject> findSearchProjectsWithPermission(
            @Param("search") String search,
            @Param("userLevel") int userLevel,
            @Param("userId") int userId,
            Pageable pageable);
    @Query("SELECT a FROM AuditProject a WHERE " +
            "(LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.created_by.assetUserName) LIKE LOWER(CONCAT('%', :search, '%'))) " )
    Page<AuditProject> findSearchProjects(
            @Param("search") String search,
            Pageable pageable);


    @Query("SELECT COUNT(a) FROM AuditProject a WHERE a.Auditor.assetUserId = :userId")
    long countByAuditorId(@Param("userId") Integer userId);

    @Query("SELECT COUNT(a) FROM AuditProject a WHERE " +
            "(:status IS NULL OR a.status = :status) " +
            "AND (:userLevel = 0 OR :userLevel = 1 AND a.Auditor.assetUserId = :userId)")
    long countWithFiltersWithPermission(
            @Param("status") Integer status,
            @Param("userLevel") int userLevel,
            @Param("userId") int userId);
    @Query("SELECT COUNT(a) FROM AuditProject a WHERE " +
            "(:status IS NULL OR a.status = :status) ")
    long countWithFilters(
            @Param("status") Integer status);

    @Query("SELECT COUNT(a) FROM AuditProject a WHERE " +
            "(LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.created_by.assetUserName) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:userLevel = 0 OR :userLevel = 1 AND a.Auditor.assetUserId = :userId)")
    long countWithSearchWithPermission(
            @Param("search") String search,
            @Param("userLevel") int userLevel,
            @Param("userId") int userId);
    @Query("SELECT COUNT(a) FROM AuditProject a WHERE " +
            "(LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.created_by.assetUserName) LIKE LOWER(CONCAT('%', :search, '%'))) ")
    long countWithSearch(
            @Param("search") String search);

}