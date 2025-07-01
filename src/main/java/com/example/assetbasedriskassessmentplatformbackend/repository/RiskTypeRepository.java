package com.example.assetbasedriskassessmentplatformbackend.repository;

import com.example.assetbasedriskassessmentplatformbackend.entity.RiskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RiskTypeRepository extends JpaRepository<RiskType, Integer> {


    @Query("SELECT rt FROM RiskType rt WHERE rt.typeID IN :typeIDs")
    List<RiskType> findByTypeIdIn(@Param("typeIDs") List<Integer> typeIDs);

    // Find a single risk type by typeID
    Optional<RiskType> findByTypeID(Integer typeID);

    // Find all risk types ordered by typeID
    @Query("SELECT rt FROM RiskType rt ORDER BY rt.typeID ASC")
    List<RiskType> findAllOrderedByTypeID();

}