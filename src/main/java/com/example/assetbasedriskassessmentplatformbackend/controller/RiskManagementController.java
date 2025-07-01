package com.example.assetbasedriskassessmentplatformbackend.controller;

import com.example.assetbasedriskassessmentplatformbackend.Service.SubRiskManagementService;
import com.example.assetbasedriskassessmentplatformbackend.entity.AssetsBasicInfo;
import com.example.assetbasedriskassessmentplatformbackend.entity.RiskType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/risk-management")
@CrossOrigin(origins = "*")
public class RiskManagementController {

    private final SubRiskManagementService subRiskManagementServiceriskService;

    @Autowired
    public RiskManagementController(SubRiskManagementService subRiskManagementServiceriskService) {
        this.subRiskManagementServiceriskService = subRiskManagementServiceriskService;
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getRiskRelationshipsCount(
            @RequestParam Integer assetId) {
        return subRiskManagementServiceriskService.getRiskRelationshipsCount(assetId);
    }

    @GetMapping("/risk-types")
    public ResponseEntity<Map<String, Object>> getRiskTypesWithContent(
            @RequestParam Integer assetId) {
        return subRiskManagementServiceriskService.getRiskTypesWithContent(assetId);
    }

    @PostMapping("/save-risk")
    public ResponseEntity<Map<String, Object>> saveRiskData(
            @RequestBody Map<String, Object> request) {

        System.out.println("[saveRiskData] Received request data: " + request);

        try {
            Integer assetId = Integer.parseInt(request.get("assetId").toString());
            Integer typeID = Integer.parseInt(request.get("typeID").toString());

            System.out.println("typeID: " + typeID);

            // 2. 处理applicable字段
            Boolean applicable = false;
            Object applicableObj = request.get("applicable");
            if (applicableObj != null) {
                applicable = "1".equals(applicableObj.toString());
            }

            // 3. 处理其他字段
            String riskOwnerUsername = request.get("riskOwner") != null ?
                    request.get("riskOwner").toString() : null;
            String comments = request.get("comments") != null ?
                    request.get("comments").toString() : null;
            String dueDateStr = request.get("dueDate") != null ?
                    request.get("dueDate").toString() : null;

            // 4. 处理isDone字段
            boolean isDone = false;
            Object isDoneObj = request.get("isDone");
            if (isDoneObj != null) {
                if (isDoneObj instanceof Boolean) {
                    isDone = (Boolean) isDoneObj;
                } else {
                    isDone = Boolean.parseBoolean(isDoneObj.toString());
                }
            }
            Integer currentUserId = Integer.parseInt(request.get("currentUserId").toString());

            // 调试打印解析后的数据
            System.out.println("Parsed data: " +
                    "assetId=" + assetId + ", " +
                    "typeID=" + typeID + ", " +
                    "applicable=" + applicable + ", " +
                    "riskOwner=" + riskOwnerUsername + ", " +
                    "comments=" + comments + ", " +
                    "dueDate=" + dueDateStr + ", " +
                    "isDone=" + isDone + ", " +
                    "currentUserId=" + currentUserId);

//---

            return subRiskManagementServiceriskService.saveRiskData(
                    assetId, typeID, applicable,
                    riskOwnerUsername, comments, dueDateStr,
                    isDone, currentUserId);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Invalid request data: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/logs")
    public ResponseEntity<Map<String, Object>> getRiskLogs(
            @RequestParam Integer assetId,
            @RequestParam Integer typeID) {
        return subRiskManagementServiceriskService.getRiskLogs(assetId, typeID);
    }

    @GetMapping("/treatment-details")
    public ResponseEntity<Map<String, Object>> getTreatmentDetails(
            @RequestParam Integer rid) {
        return subRiskManagementServiceriskService.getTreatmentDetails(rid);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "Server error: " + e.getMessage()
        ));
    }
}