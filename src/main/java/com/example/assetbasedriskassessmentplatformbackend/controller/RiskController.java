package com.example.assetbasedriskassessmentplatformbackend.controller;

import com.example.assetbasedriskassessmentplatformbackend.Service.RiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/risk")
@CrossOrigin(origins = "*")
public class RiskController {
    @Autowired
    RiskService riskService;

    @GetMapping("/assets_count_by_owner")
    public ResponseEntity<Map<String, Object>> assetsCountbyOwner(@RequestParam int userId) {
        System.out.println("risk: assetsCountbyOwner");
        return riskService.assetsCountbyOwner(userId);
    }

    @GetMapping("/getAssetsByOwner")
    public ResponseEntity<Map<String, Object>> getAssetsByOwner(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "15") int size,
                                                                @RequestParam int userId) {
        System.out.println("risk: getAssetsByOwner");
        return riskService.getAssetsByOwner(page,size,userId);
    }
}
