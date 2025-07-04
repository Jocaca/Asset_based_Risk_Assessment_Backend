package com.example.assetbasedriskassessmentplatformbackend.controller;

import com.example.assetbasedriskassessmentplatformbackend.Service.UserService;
import com.example.assetbasedriskassessmentplatformbackend.entity.AssetsBasicInfo;

import com.example.assetbasedriskassessmentplatformbackend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
@CrossOrigin(origins = "*")

public class riskrelationshipControler {

    

   @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/api/risk_relationship/{rid}")
    public String getComments(@PathVariable int rid) {

        System.out.println("Received rid: " + rid);
        String sql = "SELECT comments FROM risk_relationship WHERE rid = ?";
        
        try {
            // 执行查询，获取结果并返回
            return jdbcTemplate.queryForObject(sql, new Object[]{rid}, String.class);
        } catch (Exception e) {
            // 如果发生异常，返回错误信息
            System.err.println("Error fetching comments for rid " + rid + ": " + e.getMessage());
            return "Error fetching comments";  // 
        }
    //}
    
    
}
    
}
