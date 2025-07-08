package com.example.assetbasedriskassessmentplatformbackend.repository;

import com.example.assetbasedriskassessmentplatformbackend.entity.AuditProject;
import com.example.assetbasedriskassessmentplatformbackend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    // 原生SQL查询：支持首字母匹配和前缀匹配
    @Query(value = """
    SELECT a.*, 
        CASE WHEN LOWER(a.name) LIKE LOWER(:prefixPattern) THEN 1 ELSE 0 END AS prefix_score,
        CASE WHEN (
            SELECT CONCAT(
                SUBSTRING(LOWER(SUBSTRING_INDEX(a.name, ' ', 1)), 1, 1),
                IF(LOCATE(' ', a.name) > 0, 
                   SUBSTRING(LOWER(SUBSTRING_INDEX(SUBSTRING_INDEX(a.name, ' ', 2), ' ', -1)), 1, 1), 
                   ''),
                IF(LENGTH(a.name) - LENGTH(REPLACE(a.name, ' ', '')) > 1,
                   SUBSTRING(LOWER(SUBSTRING_INDEX(SUBSTRING_INDEX(a.name, ' ', 3), ' ', -1)), 1, 1),
                   '')
            )
        ) = LOWER(:initials) THEN 1 ELSE 0 END AS initials_score
    FROM audit_project a
    WHERE 
        LOWER(a.name) LIKE LOWER(:prefixPattern)
        OR (
            SELECT CONCAT(
                SUBSTRING(LOWER(SUBSTRING_INDEX(a.name, ' ', 1)), 1, 1),
                IF(LOCATE(' ', a.name) > 0, 
                   SUBSTRING(LOWER(SUBSTRING_INDEX(SUBSTRING_INDEX(a.name, ' ', 2), ' ', -1)), 1, 1), 
                   ''),
                IF(LENGTH(a.name) - LENGTH(REPLACE(a.name, ' ', '')) > 1,
                   SUBSTRING(LOWER(SUBSTRING_INDEX(SUBSTRING_INDEX(a.name, ' ', 3), ' ', -1)), 1, 1),
                   '')
            )
        ) = LOWER(:initials)
    ORDER BY 
        prefix_score DESC,
        initials_score DESC,
        name
    LIMIT 10
    """, nativeQuery = true)
    List<AuditProject> findByInitialsOrPrefix(
            @Param("initials") String initials,
            @Param("prefixPattern") String prefixPattern);

    // 默认方法：智能生成查询参数
    default List<AuditProject> findTop10ByInitials(String input) {
        // 生成首字母正则表达式（如 zs -> z.*s.*）
        String initialsPattern = Arrays.stream(input.split(""))
                .map(c -> c + ".*")
                .collect(Collectors.joining());

        // 生成前缀匹配模式（如 ji -> ji%）
        String prefixPattern = input.toLowerCase() + "%";

        return findByInitialsOrPrefix(
                input,
//                initialsPattern,
                prefixPattern
        );
    }
}