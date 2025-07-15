package com.example.assetbasedriskassessmentplatformbackend.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@CrossOrigin(origins = "*")

public class AuditProjectController {
     @Autowired
    private JdbcTemplate jdbcTemplate;

    // 获取所有 auditor 列为空的审计项目
    @GetMapping("/api/audit_project/available")
    public ResponseEntity<Map<String, Object>> getAvailableAuditProjects() {
        Map<String, Object> response = new HashMap<>();
        
        // SQL 查询：获取 auditor 列为空的所有审计项目
        String sql = "SELECT id, name FROM audit_project WHERE auditor IS NULL";
        
        try {
            // 执行查询，获取结果
            List<Map<String, Object>> auditProjects = jdbcTemplate.queryForList(sql);
            
            // 判断是否有结果
            if (auditProjects.isEmpty()) {
                response.put("success", false);
                response.put("message", "No available audit projects found.");
                return ResponseEntity.ok(response);
            }
            
            // 将结果封装到 response 中
            response.put("success", true);
            response.put("projects", auditProjects);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 处理异常
            System.err.println("Error fetching audit projects: " + e.getMessage());
            response.put("success", false);
            response.put("message", "Failed to fetch audit projects.");
            return ResponseEntity.status(500).body(response);
        }
    }
    
}
