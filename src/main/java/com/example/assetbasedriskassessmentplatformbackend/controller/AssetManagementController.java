package com.example.assetbasedriskassessmentplatformbackend.controller;

import com.example.assetbasedriskassessmentplatformbackend.Service.UserService;
import com.example.assetbasedriskassessmentplatformbackend.entity.AssetsBasicInfo;
import com.example.assetbasedriskassessmentplatformbackend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/asset-management")
@CrossOrigin(origins = "*")

public class AssetManagementController {

    @Autowired
    private JdbcTemplate jdbcTemplate;  // 注入 JdbcTemplate


     @PostMapping("/save")
    public ResponseEntity<String> saveAsset(@RequestBody Map<String, Object> formData) {
        //先只传了basicinfo的用于主界面演示
        // 在控制台打印接收到的数据
        System.out.println("Received asset data: " + formData);//经检验ok

       // 从 formData 获取数据
       Integer assetId;
        try {
            assetId = Integer.parseInt((String) formData.get("assetID"));  // 将 assetID 从 String 转换为 Integer
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid assetID: Must be an integer.");
        }
        String assetName = (String) formData.get("name");
        
        String assetTypeStr = (String) formData.get("AssetType");
        Integer assetType = null;  // 默认为 null

        if ("software".equalsIgnoreCase(assetTypeStr)) {
            assetType = 0;  // 0=software
        } else if ("physical".equalsIgnoreCase(assetTypeStr)) {
            assetType = 1;  // 1=physical
        } else if ("information".equalsIgnoreCase(assetTypeStr)) {
            assetType = 2;  // 2=information
        } else if ("people".equalsIgnoreCase(assetTypeStr)) {
            assetType = 3;  // 3=people
        }


        String associatedAssets = (String) formData.get("associatedAssets");
        String assetOwner = (String) formData.get("assetOwner");
        String contact = (String) formData.get("contact");
        String description = (String) formData.get("description");
        String importancestr = (String) formData.get("importance");
        Integer importance=null;

        if ("low".equalsIgnoreCase(importancestr)) {
            importance = 0;  
        } else if ("medium".equalsIgnoreCase(importancestr)) {
            importance = 1;  
        } else if ("high".equalsIgnoreCase(importancestr)) {
            importance = 2; 
        } else if ("extremelyhigh".equalsIgnoreCase(importancestr)) {
            importance = 3; 
        }

        String statusstr = (String) formData.get("status");
        Integer status=null;

        if ("active".equalsIgnoreCase(statusstr)) {
            status = 0; 
        } else if ("decommissioned".equalsIgnoreCase(statusstr)) {
            status = 1; 
        }
        
        Integer qStatus=0; //0=in_progress, 1=finished//这个也是，如果有就不变，如果是空就变0
        //传入的timestamp没啥用，就是localhost演示用一下
        // 处理缺失字段
        Date currentDate = new Date(); // 获取当前时间
        Date createdAt = currentDate;//如果原来有数据，则不覆盖


        Integer emptyFields = 1; // 默认设置为 "yes" (1) //之后传输搞一下，先这样
        String swid = ""; // 默认 SWID 为空，不知道要干啥
        Date updatedAt = currentDate;

        System.out.println("test: " + assetId+assetName+assetType+associatedAssets+contact+description+importance+qStatus+status+emptyFields+swid+createdAt+updatedAt+assetOwner);
        //测试ok

        // // 查找是否已有记录
        String selectQuery = "SELECT COUNT(*) FROM asset_risk_management.assets_basic_info WHERE asset_id = ?";
        int count = jdbcTemplate.queryForObject(selectQuery, Integer.class, assetId);
        System.out.println("count: " + count);


        if (count > 0) {
            // 如果 asset_id 存在，进行更新
            String updateQuery = "UPDATE asset_risk_management.assets_basic_info SET " +
                    "asset_name = ?, asset_type = ?, associated_assets = ?, contact = ?, description = ?, " +
                    "improtance = ?, status = ?, empty_field = ?, swid = ?, updated_at = ?, asset_owner=?" +
                    "WHERE asset_id = ?";

            jdbcTemplate.update(updateQuery, assetName, assetType, associatedAssets, contact, description,
                    importance, status, emptyFields, swid, updatedAt,assetOwner, assetId);

        } else {
            // 如果 asset_id 不存在，进行插入
            String insertQuery = "INSERT INTO asset_risk_management.assets_basic_info (" +
                    "asset_name,qstatus,asset_type,associated_assets,contact,description,status,improtance,empty_field,created_at, updated_at,asset_owner) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            //asset_id传不了，设置的是由数据库自动生成，前端要删掉还是id对用户可见？？
            //importance传不了//建表拼错了。。。。。。。。
            //System.out.println("importance: " + importance);//数值没问题

            //assetowner后端这个应该string更合理，前端api获取name然后选择。


            jdbcTemplate.update(insertQuery,assetName,qStatus,assetType,associatedAssets,contact,description,status,importance,emptyFields,createdAt,updatedAt,assetOwner);

            //         String insertQuery = "INSERT INTO asset_risk_,agement.assets_basic_info (" +
            //         "asset_id, asset_type, associated_assets, contact, description, importance, " +
            //         "qstatus, status, empty_field, swid, created_at, updated_at,asset_owner) " +
            //         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            // jdbcTemplate.update(insertQuery, assetId, assetType, associatedAssets, contact, description,
            //         importance, qStatus, status, emptyFields, swid, createdAt, updatedAt,assetOwner);
        };



    return ResponseEntity.status(HttpStatus.CREATED).body("Asset data received and saved.");
    };
}

    

