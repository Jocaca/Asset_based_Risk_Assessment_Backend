package com.example.assetbasedriskassessmentplatformbackend.Service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AssetService {
    ResponseEntity<Map<String, Object>> getAllAssets(int page,int size);
    ResponseEntity<Map<String, Object>> assetsCount();
    ResponseEntity<Map<String, Object>> getFilteredAssets(int page,int size, int assetType, int emptyField, int importance, int status);
    ResponseEntity<Map<String, Object>> FilterAssetCount(int assetType, int emptyField, int importance, int status);
    ResponseEntity<Map<String, Object>> getSearchAssets(int page,int size,String search);
    ResponseEntity<Map<String, Object>> SearchAssetsCount(String search);
}
