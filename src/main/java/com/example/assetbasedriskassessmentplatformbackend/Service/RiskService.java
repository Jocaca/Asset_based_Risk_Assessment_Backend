package com.example.assetbasedriskassessmentplatformbackend.Service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface RiskService {
    ResponseEntity<Map<String, Object>> assetsCountbyOwner(int userId);
    ResponseEntity<Map<String, Object>> getAssetsByOwner(int page,int size,int userId);
    ResponseEntity<Map<String, Object>> FilterAssetCount(int assetType, int status, int treatmentstatus, int userId);
    ResponseEntity<Map<String, Object>> getFilteredAssets(int page,int size, int assetType, int status, int treatmentstatus,int userid);
    ResponseEntity<Map<String, Object>> SearchAssetsCount(String searchTerm,int userId);
    ResponseEntity<Map<String, Object>> getSearchAssetsByOwner(int page, int size, String searchTerm, int userId);
}
