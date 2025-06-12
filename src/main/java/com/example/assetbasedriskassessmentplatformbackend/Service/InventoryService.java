package com.example.assetbasedriskassessmentplatformbackend.Service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface InventoryService {
    ResponseEntity<Map<String, Object>> getAssetInfo (int id,String type);
}
