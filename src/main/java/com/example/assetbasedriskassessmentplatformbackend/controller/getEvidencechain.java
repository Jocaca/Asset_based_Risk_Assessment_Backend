package com.example.assetbasedriskassessmentplatformbackend.controller;


import org.springframework.beans.factory.annotation.Autowired;
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
    
}
