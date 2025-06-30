package com.example.assetbasedriskassessmentplatformbackend.Service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface RiskService {
    ResponseEntity<Map<String, Object>> assetsCountbyOwner(int userId);
    ResponseEntity<Map<String, Object>> getAssetsByOwner(int page,int size,int userId);
}
