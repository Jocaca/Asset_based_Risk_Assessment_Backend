package com.example.assetbasedriskassessmentplatformbackend.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.Map;
import java.util.HashMap;

@RestController
@CrossOrigin(origins = "*")

public class DeleteUserController {

     @Autowired
    private JdbcTemplate jdbcTemplate;

    // 检查用户是否可以删除
    @GetMapping("/api/user/canDeleteUser")
    public ResponseEntity<Map<String, Object>> canDeleteUser(@RequestParam Long userId) {
        Map<String, Object> response = new HashMap<>();

        System.out.println("Received id: " + userId);
        
        // 检查用户是否是任何资产的所有者
        String assetQuery = "SELECT COUNT(*) FROM assets_basic_info WHERE asset_owner = ?";
        int assetCount = jdbcTemplate.queryForObject(assetQuery, new Object[]{userId}, Integer.class);
        System.out.println("assetCount: " + assetCount);

        // 检查用户是否是审计项目的审计员
        String auditQuery = "SELECT COUNT(*) FROM audit_project WHERE auditor = ?";
        int auditCount = jdbcTemplate.queryForObject(auditQuery, new Object[]{userId}, Integer.class);
        System.out.println("auditCount: " + auditCount);

         // 如果用户与资产或审计项目有关联，不能删除
        if (assetCount > 0 || auditCount > 0) {
            response.put("canDelete", false);
            response.put("message", "This user is associated with an asset or an audit project and cannot be deleted.");
            return ResponseEntity.ok(response);  // 返回200成功状态，但标记为不能删除
        }


        // 用户没有关联任何资产或审计项目，可以删除
        response.put("canDelete", true);
        return ResponseEntity.ok(response);  // 返回200成功状态
    }


    
}
