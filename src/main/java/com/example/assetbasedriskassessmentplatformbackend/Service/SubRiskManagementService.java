package com.example.assetbasedriskassessmentplatformbackend.Service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface SubRiskManagementService {
    ResponseEntity<Map<String, Object>> getRiskRelationshipsCount(Integer assetId);
    ResponseEntity<Map<String, Object>> getRiskTypesWithContent(Integer assetId);

    //isDone: 用于区分用户选择的是暂存 还是确定存储
    ResponseEntity<Map<String, Object>> saveRiskData(
            Integer assetId, Integer typeID, Boolean applicable,
            String riskOwnerUsername, String comments, String dueDateStr,
            Boolean isDone, Integer currentUserId);

    ResponseEntity<Map<String, Object>> getRiskLogs(Integer assetId, Integer typeID);

    ResponseEntity<Map<String, Object>> getTreatmentDetails(Integer rid);

    ResponseEntity<Map<String, Object>> getValidRiskRelationships(Integer assetId, Integer typeID);
}