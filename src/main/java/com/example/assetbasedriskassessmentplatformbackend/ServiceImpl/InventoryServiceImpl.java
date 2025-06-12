package com.example.assetbasedriskassessmentplatformbackend.ServiceImpl;

import com.example.assetbasedriskassessmentplatformbackend.Service.InventoryService;
import com.example.assetbasedriskassessmentplatformbackend.entity.AssetsPhysical;
import com.example.assetbasedriskassessmentplatformbackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class InventoryServiceImpl implements InventoryService {
    @Autowired
    AssetBasicInfoRepository assetBasicInfoRepository;
    @Autowired
    AssetSoftwareRepository assetSoftwareRepository;
    @Autowired
    AssetPhysicalRepository assetPhysicalRepository;
    @Autowired
    AssetInformationRepository assetInformationRepository;
    @Autowired
    AssetPeopleRepository assetPeopleRepository;

    public ResponseEntity<Map<String, Object>> getAssetInfo (int id, String type){
        Object asset;
        System.out.println(type);
        switch (type) {
            case "Software":
                asset = assetSoftwareRepository.findById((long)id).orElse(null);
                break;
            case "Physical":
                asset = assetPhysicalRepository.findById((long)id).orElse(null);
                break;
            case "Information":
                asset = assetInformationRepository.findById((long)id).orElse(null);
                break;
            case "People":
                asset = assetPeopleRepository.findById((long)id).orElse(null);
                break;
            default:
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Invalid asset type"
                ));
        }

        if (asset == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", asset
        ));
    }
}
