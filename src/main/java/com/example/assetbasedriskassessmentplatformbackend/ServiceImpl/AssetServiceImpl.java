package com.example.assetbasedriskassessmentplatformbackend.ServiceImpl;

import com.example.assetbasedriskassessmentplatformbackend.Service.AssetService;
import com.example.assetbasedriskassessmentplatformbackend.entity.AssetsBasicInfo;
import com.example.assetbasedriskassessmentplatformbackend.repository.AssetBasicInfoRepository;

import ch.qos.logback.classic.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AssetServiceImpl implements AssetService {
    @Autowired
    private AssetBasicInfoRepository assetBasicInfoRepository;

    public ResponseEntity<Map<String, Object>> getAllAssets(int page,int size){
        Map<String, Object> response = new HashMap<>();

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<AssetsBasicInfo> assetsPage = assetBasicInfoRepository.findAll(pageable);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 转换数据格式以匹配前端期望的结构
            List<Map<String, Object>> formattedAssets = assetsPage.getContent().stream()
                    .map(asset -> {
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
                        assetMap.put("qstatus", asset.getQStatus()==0?"In-progress":"Finished");
//                        assetMap.put("importance", asset.getImportance());
                        switch (asset.getEmptyFields()){
                            case 0: assetMap.put("emptyFields", "No");break;
                            case 1: assetMap.put("emptyFields", "Yes");break;
                        }
                        int importance = asset.getImportance();
                        switch (importance){
                            case 0: assetMap.put("importance", "Low");break;
                            case 1: assetMap.put("importance", "Medium");break;
                            case 2: assetMap.put("importance", "High");break;
                            case 3: assetMap.put("importance", "Extremely High");break;
                        }
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

    public ResponseEntity<Map<String, Object>> getFilteredAssets(int page, int size, int assetType, int emptyField, int importance, int status) {
        Map<String, Object> response = new HashMap<>();

        try {
            Pageable pageable = PageRequest.of(page, size);

            List<AssetsBasicInfo> assetsPage = assetBasicInfoRepository.findFilteredAssets(assetType,emptyField,importance,status,pageable);
//            List<AssetsBasicInfo> assetsPage = assetBasicInfoRepository.findFilteredAssetsNative(assetType,emptyField,importance,status,page*size,size);
//            System.out.println(assetsPage);

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
                        // assetMap.put("assetOwner", asset.getAssetOwner().getAssetUserName());
                        assetMap.put("status", asset.getStatus() == 0 ? "Active" : "Decommissioned");
                        assetMap.put("qstatus", asset.getQStatus()==0?"In-progress":"Finished");

                        switch (asset.getEmptyFields()) {
                            case 0: assetMap.put("emptyFields", "No"); break;
                            case 1: assetMap.put("emptyFields", "Yes"); break;
                        }

                        int assetImportance = asset.getImportance();
                        switch (assetImportance) {
                            case 0: assetMap.put("importance", "Low"); break;
                            case 1: assetMap.put("importance", "Medium"); break;
                            case 2: assetMap.put("importance", "High"); break;
                            case 3: assetMap.put("importance", "Extremely High"); break;
                        }
                        return assetMap;
                    })
                    .collect(Collectors.toList());

            response.put("success", true);
            response.put("data", formattedAssets);
//            response.put("totalElements", assetsPage.getTotalElements());
//            response.put("totalPages", assetsPage.getTotalPages());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取过滤资产数据失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> getSearchAssets(int page,int size,String search){
        Map<String, Object> response = new HashMap<>();

        try {
            Pageable pageable = PageRequest.of(page, size);

            List<AssetsBasicInfo> assetsPage = assetBasicInfoRepository.findSearchAssets(search,pageable);
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
                        // assetMap.put("assetOwner", asset.getAssetOwner().getAssetUserName());
                        assetMap.put("status", asset.getStatus() == 0 ? "Active" : "Decommissioned");
                        assetMap.put("qstatus", asset.getQStatus()==0?"In-progress":"Finished");

                        switch (asset.getEmptyFields()) {
                            case 0: assetMap.put("emptyFields", "No"); break;
                            case 1: assetMap.put("emptyFields", "Yes"); break;
                        }

                        int assetImportance = asset.getImportance();
                        switch (assetImportance) {
                            case 0: assetMap.put("importance", "Low"); break;
                            case 1: assetMap.put("importance", "Medium"); break;
                            case 2: assetMap.put("importance", "High"); break;
                            case 3: assetMap.put("importance", "Extremely High"); break;
                        }
                        return assetMap;
                    })
                    .collect(Collectors.toList());

            response.put("success", true);
            response.put("data", formattedAssets);
//            response.put("totalElements", assetsPage.getTotalElements());
//            response.put("totalPages", assetsPage.getTotalPages());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取过滤资产数据失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> getFilteredAssets_2(int page, int size, int assetType, int status, int qstatus) {
        Map<String, Object> response = new HashMap<>();

        try {
            Pageable pageable = PageRequest.of(page, size);

            List<AssetsBasicInfo> assetsPage = assetBasicInfoRepository.findFilteredAssets_2(assetType,status,qstatus,pageable);
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
                        // assetMap.put("assetOwner", asset.getAssetOwner().getAssetUserName());
                        assetMap.put("status", asset.getStatus() == 0 ? "Active" : "Decommissioned");
                        assetMap.put("qstatus", asset.getQStatus()==0?"In-progress":"Finished");

                        switch (asset.getEmptyFields()) {
                            case 0: assetMap.put("emptyFields", "No"); break;
                            case 1: assetMap.put("emptyFields", "Yes"); break;
                        }

                        int assetImportance = asset.getImportance();
                        switch (assetImportance) {
                            case 0: assetMap.put("importance", "Low"); break;
                            case 1: assetMap.put("importance", "Medium"); break;
                            case 2: assetMap.put("importance", "High"); break;
                            case 3: assetMap.put("importance", "Extremely High"); break;
                        }
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

    public ResponseEntity<Map<String, Object>> assetsCount(){
        Map<String, Object> response = new HashMap<>();
        try {
            long count = assetBasicInfoRepository.count();
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

    public ResponseEntity<Map<String, Object>> FilterAssetCount(int assetType, int emptyField, int importance, int status){
        Map<String, Object> response = new HashMap<>();
        try {
            long count = assetBasicInfoRepository.countWithFilters(assetType,emptyField,importance,status);
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

    public ResponseEntity<Map<String, Object>> SearchAssetsCount(String search){
        Map<String, Object> response = new HashMap<>();
        try {
            long count = assetBasicInfoRepository.countWithSearch(search);
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

    public ResponseEntity<Map<String, Object>> FilterAssetCount_2(int assetType, int status, int qstatus){
        Map<String, Object> response = new HashMap<>();
        try {
            long count = assetBasicInfoRepository.countWithFilters_2(assetType,status,qstatus);
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


    // ljy新增 saveAsset 方法
//     @CrossOrigin(origins = "http://localhost:8080")  // 允许来自该地址的跨域请求
//     @Override
// public ResponseEntity<Map<String, Object>> saveAsset(AssetsBasicInfo assetInfo) {
//     // 创建一个 Logger 实例来记录日志
//     org.slf4j.Logger logger = LoggerFactory.getLogger(AssetServiceImpl.class);

//     try {
//         // 打印 assetInfo 的内容，检查数据是否正常传递
//         logger.info("Received asset info: {}", assetInfo);

//         // 如果你希望查看具体的字段，可以单独打印每个字段
//         // logger.info("Asset Name: {}", assetInfo.getName());
//         // logger.info("Asset ID: {}", assetInfo.getAssetID());
//         // logger.info("Asset Type: {}", assetInfo.getAssetType());
//         // 按照你需要的字段继续打印...

//         // 模拟保存资产数据，这里可以用你实际的保存代码
//         // assetsBasicInfoRepository.save(assetInfo);

//         return ResponseEntity.ok("资产保存成功！");
//     } catch (Exception e) {
//         logger.error("保存资产时发生错误: {}", e.getMessage(), e);
//         return ResponseEntity.status(500).body("保存资产时发生错误：" + e.getMessage());
//     }
// }

}
