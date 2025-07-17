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

    @GetMapping("/api/audit_project/available")
public ResponseEntity<Map<String, Object>> getAvailableAuditProjects() {
    Map<String, Object> response = new HashMap<>();
    
    // 修改后的 SQL 查询：获取所有审计项目，不再限制 auditor 列为空
    String sql = "SELECT id, name FROM audit_project";
    
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
};


      // 更新审计项目的 auditor
    @PostMapping("/api/updateauditproject/{auditProjectId}/{userId}")
    public ResponseEntity<Map<String, Object>> updateAuditProjectAuditor(
            @PathVariable int auditProjectId, @PathVariable int userId) {
        
        Map<String, Object> response = new HashMap<>();
        
        // SQL 查询：更新指定审计项目的 auditor 列
        String sql = "UPDATE audit_project SET auditor = ? WHERE id = ?";
        
        try {
            // 执行更新操作
            int rowsAffected = jdbcTemplate.update(sql, userId, auditProjectId);
            
            // 如果没有更新任何行，说明没有找到对应的审计项目
            if (rowsAffected == 0) {
                response.put("success", false);
                response.put("message", "Audit project not found.");
                return ResponseEntity.status(404).body(response);
            }
            
            // 返回成功响应
            response.put("success", true);
            //response.put("message", "Audit project auditor updated successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 处理异常
            System.err.println("Error updating audit project auditor: " + e.getMessage());
            response.put("success", false);
            response.put("message", "Failed to update auditor.");
            return ResponseEntity.status(500).body(response);
        }
    };
    
}
