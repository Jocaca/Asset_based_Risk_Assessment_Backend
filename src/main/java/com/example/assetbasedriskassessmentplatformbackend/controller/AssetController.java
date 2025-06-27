package com.example.assetbasedriskassessmentplatformbackend.controller;

import com.example.assetbasedriskassessmentplatformbackend.Service.AssetService;
import com.example.assetbasedriskassessmentplatformbackend.entity.AssetsBasicInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/asset")
@CrossOrigin(origins = "*")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @GetMapping("/Allassets")
    public ResponseEntity<Map<String, Object>> getAllAssets(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "15") int size) {
        System.out.println("getAllAssets");
        return assetService.getAllAssets(page,size);
    }
    @GetMapping("/filteredAssets")
    public ResponseEntity<Map<String, Object>> getFilteredAssets(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "15") int size,
                                                                 @RequestParam(required = false) String assetType,  // ✅ 允许为空
                                                                 @RequestParam(required = false) String emptyField,
                                                                 @RequestParam(required = false) String importance,
                                                                 @RequestParam(required = false) String status) {
        System.out.println("getFilteredAssets");
        int assettype = 0;
        if (assetType == null) {assettype=-1;}
        else {
            switch (assetType) {
                case "Software":
                    assettype = 0;
                    break;
                case "Physical":
                    assettype = 1;
                    break;
                case "Information":
                    assettype = 2;
                    break;
                case "People":
                    assettype = 3;
                    break;
                default:
                    assettype = -1;
            }
        }
        int emptyfield = 0;
        if(emptyField == null) {emptyfield=-1;}
        else {
            switch (emptyField) {
                case "Yes":
                    emptyfield = 1;
                    break;
                case "No":
                    emptyfield = 0;
                    break;
                default:
                    emptyfield = -1;
            }
        }
        int Importance = 0;
        if(importance == null) {Importance=-1;}
        else {
            switch (importance) {
                case "Extremely High":
                    Importance = 3;
                    break;
                case "High":
                    Importance = 2;
                    break;
                case "Medium":
                    Importance = 1;
                    break;
                case "Low":
                    Importance = 0;
                    break;
                default:
                    Importance = -1;
            }
        }
        int Status = 0;
        if(status==null) {Status=-1;}
        else {
            switch (status) {
                case "Active":
                    Status = 0;
                    break;
                case "Decommissioned":
                    Status = 1;
                    break;
                default:
                    Status = -1;
            }
        }
        return assetService.getFilteredAssets(page,size,assettype,emptyfield,Importance,Status);
    }
    @GetMapping("/filteredAssets_2")
    public ResponseEntity<Map<String, Object>> getFilteredAssets_2(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "15") int size,
                                                                   @RequestParam(required = false) String assetType,
                                                                   @RequestParam(required = false) String status,
                                                                   @RequestParam(required = false) String qstatus) {
        System.out.println("getFilteredAssets_2");
        int assettype = 0;
        if (assetType == null) {assettype=-1;}
        else {
            switch (assetType) {
                case "Software":
                    assettype = 0;
                    break;
                case "Physical":
                    assettype = 1;
                    break;
                case "Information":
                    assettype = 2;
                    break;
                case "People":
                    assettype = 3;
                    break;
                default:
                    assettype = -1;
            }
        }
        int Qstatus = 0;
        if(qstatus == null) {Qstatus=-1;}
        else {
            switch (qstatus) {
                case "Finished":
                    Qstatus = 1;
                    break;
                case "In-progress":
                    Qstatus = 0;
                    break;
                default:
                    Qstatus = -1;
            }
        }
        int Status = 0;
        if(status==null) {Status=-1;}
        else {
            switch (status) {
                case "Active":
                    Status = 0;
                    break;
                case "Decommissioned":
                    Status = 1;
                    break;
                default:
                    Status = -1;
            }
        }
        return assetService.getFilteredAssets_2(page,size,assettype,Status,Qstatus);
    }
    @GetMapping("/filteredAssets_3")
    public ResponseEntity<Map<String, Object>> getFilteredAssets_3(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "15") int size,
                                                                   @RequestParam(required = false) String assetType,
                                                                   @RequestParam(required = false) String status,
                                                                   @RequestParam(required = false) String rtstatus) {
        System.out.println("getFilteredAssets_3");
        int assettype = 0;
        if (assetType == null) {assettype=-1;}
        else {
            switch (assetType) {
                case "Software":
                    assettype = 0;
                    break;
                case "Physical":
                    assettype = 1;
                    break;
                case "Information":
                    assettype = 2;
                    break;
                case "People":
                    assettype = 3;
                    break;
                default:
                    assettype = -1;
            }
        }
        int RTstatus = 0;
        if(rtstatus == null) {RTstatus=-1;}
        else {
            switch (rtstatus) {
                case "Finished":
                    RTstatus = 1;
                    break;
                case "In-progress":
                    RTstatus = 0;
                    break;
                default:
                    RTstatus = -1;
            }
        }
        int Status = 0;
        if(status==null) {Status=-1;}
        else {
            switch (status) {
                case "Active":
                    Status = 0;
                    break;
                case "Decommissioned":
                    Status = 1;
                    break;
                default:
                    Status = -1;
            }
        }
        return assetService.getFilteredAssets_3(page,size,assettype,Status,RTstatus);
    }
    @GetMapping("/searchAssets")
    public ResponseEntity<Map<String, Object>> getSearchAssets(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "15") int size,
                                                               @RequestParam String searchTerm) {
        System.out.println("getSearchAssets");
        return assetService.getSearchAssets(page,size,searchTerm);
    }
    @GetMapping("/assets_count")
    public ResponseEntity<Map<String, Object>> assetsCount() {
        System.out.println("assetsCount");
        return assetService.assetsCount();
    }
    @GetMapping("/filter_assets_count")
    public ResponseEntity<Map<String, Object>> FilterAssetCount(@RequestParam(required = false) String assetType,  // ✅ 允许为空
                                                                @RequestParam(required = false) String emptyField,
                                                                @RequestParam(required = false) String importance,
                                                                @RequestParam(required = false) String status) {
        System.out.println("filter_assets_count");
        int assettype = 0;
        if (assetType == null) {assettype=-1;}
        else {
            switch (assetType) {
                case "Software":
                    assettype = 0;
                    break;
                case "Physical":
                    assettype = 1;
                    break;
                case "Information":
                    assettype = 2;
                    break;
                case "People":
                    assettype = 3;
                    break;
                default:
                    assettype = -1;
            }
        }
        int emptyfield = 0;
        if(emptyField == null) {emptyfield=-1;}
        else {
            switch (emptyField) {
                case "Yes":
                    emptyfield = 1;
                    break;
                case "No":
                    emptyfield = 0;
                    break;
                default:
                    emptyfield = -1;
            }
        }
        int Importance = 0;
        if(importance == null) {Importance=-1;}
        else {
            switch (importance) {
                case "Extremely High":
                    Importance = 3;
                    break;
                case "High":
                    Importance = 2;
                    break;
                case "Medium":
                    Importance = 1;
                    break;
                case "Low":
                    Importance = 0;
                    break;
                default:
                    Importance = -1;
            }
        }
        int Status = 0;
        if(status==null) {Status=-1;}
        else {
            switch (status) {
                case "Active":
                    Status = 0;
                    break;
                case "Decommissioned":
                    Status = 1;
                    break;
                default:
                    Status = -1;
            }
        }
        return assetService.FilterAssetCount(assettype,emptyfield,Importance,Status);
    }
    @GetMapping("/search_assets_count")
    public ResponseEntity<Map<String, Object>> SearchAssetsCount(@RequestParam String searchTerm) {
        System.out.println("SearchAssetsCount");
        return assetService.SearchAssetsCount(searchTerm);
    }
    @GetMapping("/filter_assets_count_2")
    public ResponseEntity<Map<String, Object>> FilterAssetCount_2(@RequestParam(required = false) String assetType,
                                                                @RequestParam(required = false) String status,
                                                                  @RequestParam(required = false) String qstatus) {
        System.out.println("filter_assets_count_2");
        int assettype = 0;
        if (assetType == null) {assettype=-1;}
        else {
            switch (assetType) {
                case "Software":
                    assettype = 0;
                    break;
                case "Physical":
                    assettype = 1;
                    break;
                case "Information":
                    assettype = 2;
                    break;
                case "People":
                    assettype = 3;
                    break;
                default:
                    assettype = -1;
            }
        }
        int Qstatus = 0;
        if(qstatus == null) {Qstatus=-1;}
        else {
            switch (qstatus) {
                case "Finished":
                    Qstatus = 1;
                    break;
                case "In-progress":
                    Qstatus = 0;
                    break;
                default:
                    Qstatus = -1;
            }
        }
        int Status = 0;
        if(status==null) {Status=-1;}
        else {
            switch (status) {
                case "Active":
                    Status = 0;
                    break;
                case "Decommissioned":
                    Status = 1;
                    break;
                default:
                    Status = -1;
            }
        }
        return assetService.FilterAssetCount_2(assettype,Status,Qstatus);
    }
    @GetMapping("/filter_assets_count_3")
    public ResponseEntity<Map<String, Object>> FilterAssetCount_3(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "15") int size,
                                                                   @RequestParam(required = false) String assetType,
                                                                   @RequestParam(required = false) String status,
                                                                   @RequestParam(required = false) String rtstatus) {
        System.out.println("getFilteredAssets_3");
        int assettype = 0;
        if (assetType == null) {assettype=-1;}
        else {
            switch (assetType) {
                case "Software":
                    assettype = 0;
                    break;
                case "Physical":
                    assettype = 1;
                    break;
                case "Information":
                    assettype = 2;
                    break;
                case "People":
                    assettype = 3;
                    break;
                default:
                    assettype = -1;
            }
        }
        int RTstatus = 0;
        if(rtstatus == null) {RTstatus=-1;}
        else {
            switch (rtstatus) {
                case "Finished":
                    RTstatus = 1;
                    break;
                case "In-progress":
                    RTstatus = 0;
                    break;
                default:
                    RTstatus = -1;
            }
        }
        int Status = 0;
        if(status==null) {Status=-1;}
        else {
            switch (status) {
                case "Active":
                    Status = 0;
                    break;
                case "Decommissioned":
                    Status = 1;
                    break;
                default:
                    Status = -1;
            }
        }
        return assetService.FilterAssetCount_3(assettype,Status,RTstatus);
    }
}