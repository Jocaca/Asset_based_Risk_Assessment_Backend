package com.example.assetbasedriskassessmentplatformbackend.ServiceImpl;

import com.example.assetbasedriskassessmentplatformbackend.Service.RiskService;
import com.example.assetbasedriskassessmentplatformbackend.entity.AssetsBasicInfo;
import com.example.assetbasedriskassessmentplatformbackend.entity.RiskRelationship;
import com.example.assetbasedriskassessmentplatformbackend.entity.User;
import com.example.assetbasedriskassessmentplatformbackend.repository.AssetBasicInfoRepository;
import com.example.assetbasedriskassessmentplatformbackend.repository.RiskRelationshipRepository;
import com.example.assetbasedriskassessmentplatformbackend.repository.RiskTreatmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RiskServiceImpl implements RiskService {
    @Autowired
    AssetBasicInfoRepository assetBasicInfoRepository;
    @Autowired
    RiskRelationshipRepository riskRelationshipRepository;
    @Autowired
    RiskTreatmentRepository riskTreatmentRepository;

    public ResponseEntity<Map<String, Object>> assetsCountbyOwner(int userId){
        Map<String, Object> response = new HashMap<>();
        try {
            long count = assetBasicInfoRepository.countWithValidRisksByOwner(userId);
            System.out.println(count);
            response.put("success", true);
            response.put("count", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取资产数量失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> getAssetsByOwner(int page,int size,int userId){
        Map<String, Object> response = new HashMap<>();

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<AssetsBasicInfo> assetsPage = assetBasicInfoRepository.findAssetsWithValidRisksByOwner(userId,pageable);
            System.out.println("--------------------------"+assetsPage+"------------------------");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 转换数据格式以匹配前端期望的结构
            List<Map<String, Object>> formattedAssets = assetsPage.stream()
                    .map(asset -> {
                        System.out.println("------------------------"+asset+"----------------------------");
                        Map<String, Object> assetMap = new HashMap<>();
                        assetMap.put("id", asset.getAssetId());
                        assetMap.put("dateAdded", dateFormat.format(asset.getUpdatedAt()));
                        assetMap.put("name", asset.getAssetName());
                        int type =asset.getAssetType();
                        switch (type){
                            case 0: assetMap.put("assetType", "Software");break;
                            case 1: assetMap.put("assetType", "Physical");break;
                            case 2: assetMap.put("assetType", "Information");break;
                            case 3: assetMap.put("assetType", "People");break;
                        }
                        assetMap.put("assetOwner", asset.getAssetOwner().getAssetUserName());
                        assetMap.put("status", asset.getStatus()==0?"Active":"Decommissioned");
                        List<Map<String,Object>> assetRisks = new ArrayList<>();
                        for(RiskRelationship risk:asset.getRisks()){
                            Map<String, Object> assetRisk = new HashMap<>();
                            System.out.println("------------------------"+risk.getRID() +"----------------------------");
                            System.out.println("------------------------"+ Optional.ofNullable(risk.getRiskOwner()).isEmpty()+"----------------------------");
                            if(Optional.ofNullable(risk.getRiskOwner()).isPresent()&& risk.getRiskOwner().getAssetUserId() == userId && risk.getValid() == 2) {
                                assetRisk.put("risk", risk.getRiskType().getContent());
                                assetRisk.put("due", dateFormat.format(risk.getDueDate()));
                                assetRisk.put("id", risk.getRID());
                                assetRisk.put("treatmentStatus", risk.getTreatmentStatus() == 0 ? "In-progress" : "Finished");
                                assetRisks.add(assetRisk);
                            }
                        }
                        assetMap.put("assetRisks", assetRisks);
                        return assetMap;
                    })
                    .collect(Collectors.toList());

            response.put("success", true);
            response.put("data", formattedAssets);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取资产数据失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    public ResponseEntity<Map<String, Object>> FilterAssetCount(int assetType, int status, int treatmentstatus, int userId){
        Map<String, Object> response = new HashMap<>();
        try {
            long count = assetBasicInfoRepository.countWithFilters_4(assetType,status,treatmentstatus,userId);
            System.out.println(count);
            response.put("success", true);
            response.put("count", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取资产数量失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    public ResponseEntity<Map<String, Object>> getFilteredAssets(int page, int size, int assetType, int status, int treatmentstatus,int userid) {
        Map<String, Object> response = new HashMap<>();

        try {
            Pageable pageable = PageRequest.of(page, size);

            List<AssetsBasicInfo> assetsPage = assetBasicInfoRepository.findFilteredAssets_4(assetType,status,treatmentstatus,userid,pageable);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 转换数据格式以匹配前端期望的结构
            List<Map<String, Object>> formattedAssets = assetsPage.stream()
                    .map(asset -> {
                        Map<String, Object> assetMap = new HashMap<>();
                        assetMap.put("id", asset.getAssetId());
                        assetMap.put("dateAdded", dateFormat.format(asset.getUpdatedAt()));
                        assetMap.put("name", asset.getAssetName());
                        int type = asset.getAssetType();
                        switch (type) {
                            case 0: assetMap.put("assetType", "Software"); break;
                            case 1: assetMap.put("assetType", "Physical"); break;
                            case 2: assetMap.put("assetType", "Information"); break;
                            case 3: assetMap.put("assetType", "People"); break;
                        }
                        assetMap.put("assetOwner", asset.getAssetOwner().getAssetUserName());
                        assetMap.put("status", asset.getStatus() == 0 ? "Active" : "Decommissioned");
                        List<Map<String,Object>> assetRisks = new ArrayList<>();
                        for(RiskRelationship risk:asset.getRisks()){
                            Map<String, Object> assetRisk = new HashMap<>();
                            if(risk.getRiskOwner().getAssetUserId() == userid && risk.getValid() == 2) {
                                assetRisk.put("risk", risk.getRiskType().getContent());
                                assetRisk.put("due", dateFormat.format(risk.getDueDate()));
                                assetRisk.put("id", risk.getRID());
                                assetRisk.put("treatmentStatus", risk.getTreatmentStatus() == 0 ? "In-progress" : "Finished");
                                assetRisks.add(assetRisk);
                            }
                        }
                        assetMap.put("assetRisks", assetRisks);
                        return assetMap;
                    })
                    .collect(Collectors.toList());

            response.put("success", true);
            response.put("data", formattedAssets);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取过滤资产数据失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    public ResponseEntity<Map<String, Object>> SearchAssetsCount(String searchTerm,int userId){
        Map<String, Object> response = new HashMap<>();
        try {
            long count = assetBasicInfoRepository.countWithSearch_3(searchTerm,userId);
            System.out.println(count);
            response.put("success", true);
            response.put("count", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取搜索数量失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    public ResponseEntity<Map<String, Object>> getSearchAssetsByOwner(int page, int size, String searchTerm, int userId){
        Map<String, Object> response = new HashMap<>();

        try {
            Pageable pageable = PageRequest.of(page, size);

            List<AssetsBasicInfo> assetsPage = assetBasicInfoRepository.findSearchAssetsByOwner_2(searchTerm,userId,pageable);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 转换数据格式以匹配前端期望的结构
            List<Map<String, Object>> formattedAssets = assetsPage.stream()
                    .map(asset -> {
                        Map<String, Object> assetMap = new HashMap<>();
                        assetMap.put("id", asset.getAssetId());
                        assetMap.put("dateAdded", dateFormat.format(asset.getUpdatedAt()));
                        assetMap.put("name", asset.getAssetName());
                        int type = asset.getAssetType();
                        switch (type) {
                            case 0: assetMap.put("assetType", "Software"); break;
                            case 1: assetMap.put("assetType", "Physical"); break;
                            case 2: assetMap.put("assetType", "Information"); break;
                            case 3: assetMap.put("assetType", "People"); break;
                        }
                        assetMap.put("assetOwner", asset.getAssetOwner().getAssetUserName());
                        assetMap.put("status", asset.getStatus() == 0 ? "Active" : "Decommissioned");
                        List<Map<String,Object>> assetRisks = new ArrayList<>();
                        for(RiskRelationship risk:asset.getRisks()){
                            Map<String, Object> assetRisk = new HashMap<>();
                            if(risk.getRiskOwner().getAssetUserId() == userId && risk.getValid() == 2) {
                                assetRisk.put("risk", risk.getRiskType().getContent());
                                assetRisk.put("due", dateFormat.format(risk.getDueDate()));
                                assetRisk.put("id", risk.getRID());
                                assetRisk.put("treatmentStatus", risk.getTreatmentStatus() == 0 ? "In-progress" : "Finished");
                                assetRisks.add(assetRisk);
                            }
                        }
                        assetMap.put("assetRisks", assetRisks);
                        return assetMap;
                    })
                    .collect(Collectors.toList());

            response.put("success", true);
            response.put("data", formattedAssets);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取过滤资产数据失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
