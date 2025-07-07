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

    @Query("SELECT a FROM AuditProject a WHERE " +
            "(:status = -1 OR a.status = :status)")
    List<AuditProject> findFilteredProjects(
            @Param("status") Integer status,
            Pageable pageable);

    @Query("SELECT COUNT(a) FROM AuditProject a WHERE " +
            "(:status = -1 OR a.status = :status)")
    long countWithFilters(@Param("status") Integer status);

    @Query("SELECT a FROM AuditProject a WHERE " +
            "LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.created_by.assetUserName) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<AuditProject> findSearchProjects(
            @Param("search") String search,
            Pageable pageable);

    @Query("SELECT COUNT(a) FROM AuditProject a WHERE " +
            "LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(a.created_by.assetUserName) LIKE LOWER(CONCAT('%', :search, '%'))")
    long countWithSearch(@Param("search") String search);
}