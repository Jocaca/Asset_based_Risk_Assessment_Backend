package com.example.assetbasedriskassessmentplatformbackend.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface InventoryService {
    ResponseEntity<Map<String, Object>> getAssetInfo (int id,String type);
    ResponseEntity<Map<String,Object>> SaveInevntory(Map<String, Object> requestData);
}
