package com.example.assetbasedriskassessmentplatformbackend.repository;

import com.example.assetbasedriskassessmentplatformbackend.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RiskTreatmentRepository extends JpaRepository<RiskTreatment, Integer> {
    Optional<RiskTreatment> findByRiskRelationshipAndValid(RiskRelationship riskRelationship, Integer valid);


}




