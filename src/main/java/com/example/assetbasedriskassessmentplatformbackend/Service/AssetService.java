package com.example.assetbasedriskassessmentplatformbackend.Service;

import org.springframework.http.ResponseEntity;

import com.example.assetbasedriskassessmentplatformbackend.entity.AssetsBasicInfo;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface AssetService {
    ResponseEntity<Map<String, Object>> getAllAssets(int page,int size);
    ResponseEntity<Map<String, Object>> getAssetsByOwner(int page,int size,int userId);
    ResponseEntity<Map<String, Object>> assetsCount();
    ResponseEntity<Map<String, Object>> assetsCountbyOwner(int userId);
    ResponseEntity<Map<String, Object>> getFilteredAssets(int page,int size, int assetType, int emptyField, int importance, int status);
    ResponseEntity<Map<String, Object>> FilterAssetCount(int assetType, int emptyField, int importance, int status);
    ResponseEntity<Map<String, Object>> getSearchAssets(int page,int size,String search);
    ResponseEntity<Map<String, Object>> getSearchAssetsByOwner(int page, int size, String searchTerm, int userId);
    ResponseEntity<Map<String, Object>> SearchAssetsCount(String search);
    ResponseEntity<Map<String, Object>> SearchAssetsCount_2(String searchTerm,int userId);
    ResponseEntity<Map<String, Object>> FilterAssetCount_2(int assetType, int status, int qstatus);
    ResponseEntity<Map<String, Object>> getFilteredAssets_2(int page,int size, int assetType, int status, int qstatus);
    ResponseEntity<Map<String, Object>> FilterAssetCount_3(int assetType, int status, int rtstatus, int userId);
    ResponseEntity<Map<String, Object>> getFilteredAssets_3(int page,int size, int assetType, int status, int rtstatus,int userid);

}
