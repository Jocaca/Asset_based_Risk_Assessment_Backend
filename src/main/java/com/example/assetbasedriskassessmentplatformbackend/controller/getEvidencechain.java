package com.example.assetbasedriskassessmentplatformbackend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")

public class getEvidencechain {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/api/evidence_chain/{auditid}")
    public List<Map<String, Object>> getEvidenceChain(@PathVariable int auditid) {
        // 打印收到的 auditid
        System.out.println("Received auditid: " + auditid);

        // 查询 SQL，获取与传递的 auditid 匹配的所有数据
        String sql = "SELECT * FROM evidence_chain WHERE audit_project = ?";

        try {
            // 执行查询，获取结果并返回
            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, auditid);
            return result;
        } catch (Exception e) {
            // 如果发生异常，打印错误信息并返回空结果
            System.err.println("Error fetching evidence chain for auditid " + auditid + ": " + e.getMessage());
            return List.of();  // 返回空列表
        }
        //return List.of() ;
    }

      // 删除操作
      //只能全部删除 因为auditid不能为空
    // @DeleteMapping("/api/remove/{id}")
    // public ResponseEntity<String> removeEvidenceChain(@PathVariable Long id) {

    //     System.out.println("Received auditid: " + id);
    //     // SQL 删除语句，删除指定 ID 的记录
    //     String sql = "DELETE FROM evidence_chain WHERE id = ?";

    //     try {
    //         int rowsAffected = jdbcTemplate.update(sql, id);
    //         if (rowsAffected > 0) {
    //             return ResponseEntity.ok("Successfully deleted the evidence chain");
    //         } else {
    //             return ResponseEntity.status(404).body("Evidence chain with ID " + id + " not found");
    //         }
    //     } catch (Exception e) {
    //         // 处理异常
    //         System.err.println("Error removing evidence chain for ID " + id + ": " + e.getMessage());
    //         return ResponseEntity.status(500).body("Error occurred while deleting the evidence chain");
    //     }
    // }

    //  // 更新操作：只清空对应 ID 行的 audit_project 字段
    @PatchMapping("/api/remove/{id}")
    public ResponseEntity<String> removeAuditProject(@PathVariable Long id) {
        // SQL 更新语句，清空 audit_project 字段
        String sql = "UPDATE evidence_chain SET audit_project = NULL WHERE id = ?";
        System.out.println("Received id: " + id);

        try {
            int rowsAffected = jdbcTemplate.update(sql, id);
            if (rowsAffected > 0) {
                return ResponseEntity.ok("Successfully cleared the audit_project field");
            } else {
                return ResponseEntity.status(404).body("Evidence chain with ID " + id + " not found");
            }
        } catch (Exception e) {
            // 处理异常
            System.err.println("Error clearing audit_project for ID " + id + ": " + e.getMessage());
            return ResponseEntity.status(500).body("Error occurred while clearing the audit_project field");
        }
    }


     // 更新操作：修改 status 字段为 1
    @PatchMapping("/api/changeprojectstatus/{auditid}")
    public ResponseEntity<String> changeProjectStatus(@PathVariable Long auditid) {
        String sql = "UPDATE audit_project SET status = 1 WHERE id = ?";

        try {
            int rowsAffected = jdbcTemplate.update(sql, auditid);
            if (rowsAffected > 0) {
                return ResponseEntity.ok("Audit project status updated successfully.");
            } else {
                return ResponseEntity.status(404).body("Audit project with ID " + auditid + " not found.");
            }
        } catch (Exception e) {
            // 处理异常
            System.err.println("Error updating status for audit project with ID " + auditid + ": " + e.getMessage());
            return ResponseEntity.status(500).body("Error occurred while updating the project status.");
        }
    }





    
}
