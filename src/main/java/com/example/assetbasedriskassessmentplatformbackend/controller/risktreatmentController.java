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
@RequestMapping("/api/risk_treatment")
@CrossOrigin(origins = "*")

public class risktreatmentController {
      @Autowired
    private JdbcTemplate jdbcTemplate;

    // 更新或插入记录
    @PostMapping("/save/{id}")
    public ResponseEntity<String> saverisktreatment(@PathVariable int id, @RequestBody Map<String, Object> formData) {
        
          
            System.out.println("Received asset data: " + formData);//经检验ok
        System.out.println(id);

            String risklevelStr = (String) formData.get("RiskLevel");

             Integer risklevel = null;  // 默认为 null

        if ("verylow".equalsIgnoreCase(risklevelStr)) {
            risklevel = 0; 
        } else if ("low".equalsIgnoreCase(risklevelStr)) {
            risklevel = 1; 
        } else if ("medium".equalsIgnoreCase(risklevelStr)) {
            risklevel = 2; 
        } else if ("high".equalsIgnoreCase(risklevelStr)) {
            risklevel = 3; 
        }else if ("veryhigh".equalsIgnoreCase(risklevelStr)) {
            risklevel = 4;  
        }

         String treatmentoptionStr = (String) formData.get("TreatmentOption");

        Integer treatmentoption = null;  // 默认为 null

        if ("RiskAvoidance".equalsIgnoreCase(treatmentoptionStr)) {
            treatmentoption = 0; 
        } else if ("RiskModification".equalsIgnoreCase(treatmentoptionStr)) {
            treatmentoption = 1; 
        } else if ("RiskRetention".equalsIgnoreCase(treatmentoptionStr)) {
            treatmentoption = 2; 
        } else if ("RiskSharing".equalsIgnoreCase(treatmentoptionStr)) {
            treatmentoption = 3; 
        }
      

            

        String comments = (String) formData.get("comments");

        String validStr = (String) formData.get("Done");

        Integer valid = null;  // 默认为 null

        if ("Finished".equalsIgnoreCase(validStr)) {
            valid = 1; 
        } else if ("In-progress".equalsIgnoreCase(validStr)) {
            valid = 0; 
        }
        Date currentDate = new Date(); // 获取当前时间
        //这个时间只有valid是1时修改
           


        // // 查找是否已有记录
         String checkSql = "SELECT COUNT(*) FROM risk_treatment WHERE id = ?";
        int count = jdbcTemplate.queryForObject(checkSql, new Object[]{id}, Integer.class);

        System.out.println("count: " + count);//经检验ok

       
        // 插入，然后id自己生成
        String insertQuery = "INSERT INTO risk_treatment (" +
                    "risk_relationship,risk_level,treatment_option,comments,valid,update_date,id) " +
                    "VALUES (?,?,?,?,?,?,?)";
        

        jdbcTemplate.update(insertQuery,id,risklevel,treatmentoption,comments,valid,currentDate,id);

        // if(valid==1)
        // {
        //         String updateQuery3 = "UPDATE risk_treatment SET " +
        //             "update_date = ?" +
        //             "WHERE id = ?";
                
        //             jdbcTemplate.update(updateQuery3,currentDate,id);

        // }

          

   


         
        
             
            return new ResponseEntity<>("Data updated successfully", HttpStatus.OK);

       
}

    
}
