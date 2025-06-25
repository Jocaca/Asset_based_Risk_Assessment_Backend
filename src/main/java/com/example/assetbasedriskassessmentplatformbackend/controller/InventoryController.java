package com.example.assetbasedriskassessmentplatformbackend.controller;

import com.example.assetbasedriskassessmentplatformbackend.Service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/getAssetInfo")
    public ResponseEntity<Map<String, Object>> getAssetInfo(@RequestParam int id,
                                                            @RequestParam(defaultValue = "no") String type) {
        System.out.println("getAssetInfo");
        return inventoryService.getAssetInfo(id,type);
    }
    @PostMapping("/save")
    public ResponseEntity<Map<String,Object>> SaveInevntory(@RequestBody Map<String, Object> requestData){
        System.out.println("save");
        System.out.println(requestData);
        return inventoryService.SaveInevntory(requestData);
    }
}
