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
    @GetMapping("/filteredAssets")
    public ResponseEntity<Map<String, Object>> getFilteredAssets_3(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "15") int size,
                                                                   @RequestParam(required = false) String assetType,
                                                                   @RequestParam(required = false) String status,
                                                                   @RequestParam(required = false) String treatmentstatus,
                                                                   @RequestParam int userId) {
        System.out.println("risk:getFilteredAssets");
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
        int Tstatus = 0;
        if(treatmentstatus == null) {Tstatus=-1;}
        else {
            switch (treatmentstatus) {
                case "Finished":
                    Tstatus = 1;
                    break;
                case "In-progress":
                    Tstatus = 0;
                    break;
                default:
                    Tstatus = -1;
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
        return riskService.getFilteredAssets(page,size,assettype,Status,Tstatus,userId);
    }
    @GetMapping("/filter_assets_count")
    public ResponseEntity<Map<String, Object>> FilterAssetCount(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "15") int size,
                                                                  @RequestParam(required = false) String assetType,
                                                                  @RequestParam(required = false) String status,
                                                                  @RequestParam(required = false) String treatmentstatus,
                                                                  @RequestParam int userId) {
        System.out.println("risk: getFilteredAssets");
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
        int Tstatus = 0;
        if(treatmentstatus == null) {Tstatus=-1;}
        else {
            switch (treatmentstatus) {
                case "Finished":
                    Tstatus = 1;
                    break;
                case "In-progress":
                    Tstatus = 0;
                    break;
                default:
                    Tstatus = -1;
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
        return riskService.FilterAssetCount(assettype,Status,Tstatus,userId);
    }

    @GetMapping("/search_assets_count")
    public ResponseEntity<Map<String, Object>> SearchAssetsCount_2(@RequestParam String searchTerm,
                                                                   @RequestParam int userId) {
        System.out.println("risk: SearchAssetsCount");
        return riskService.SearchAssetsCount(searchTerm,userId);
    }

    @GetMapping("/searchAssetsByOwner")
    public ResponseEntity<Map<String, Object>> getSearchAssetsByOwner(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "15") int size,
                                                                      @RequestParam String searchTerm,
                                                                      @RequestParam int userId) {
        System.out.println("risk: etSearchAssets");
        return riskService.getSearchAssetsByOwner(page,size,searchTerm,userId);
    }
}
