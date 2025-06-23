package com.example.assetbasedriskassessmentplatformbackend.Service;

import org.springframework.http.ResponseEntity;

import com.example.assetbasedriskassessmentplatformbackend.entity.AssetsBasicInfo;

import java.util.Map;

public interface AssetService {
    ResponseEntity<Map<String, Object>> getAllAssets(int page,int size);
    ResponseEntity<Map<String, Object>> assetsCount();
    ResponseEntity<Map<String, Object>> getFilteredAssets(int page,int size, int assetType, int emptyField, int importance, int status);
    ResponseEntity<Map<String, Object>> FilterAssetCount(int assetType, int emptyField, int importance, int status);
    ResponseEntity<Map<String, Object>> getSearchAssets(int page,int size,String search);
    ResponseEntity<Map<String, Object>> SearchAssetsCount(String search);
    ResponseEntity<Map<String, Object>> FilterAssetCount_2(int assetType, int status, int qstatus);
    ResponseEntity<Map<String, Object>> getFilteredAssets_2(int page,int size, int assetType, int status, int qstatus);
    //  // ljy新加，用于保存资产
    // ResponseEntity<Map<String, Object>> saveAsset(AssetsBasicInfo assetInfo);
}
