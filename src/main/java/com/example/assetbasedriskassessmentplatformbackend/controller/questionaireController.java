
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

// import static org.mockito.Mockito.description;

import java.util.Date;
import java.util.Map;

// import com.example.assetbasedriskassessmentplatformbackend.dto.QuestionaireRequest;

@RestController
@RequestMapping("/api/questionaire")
@CrossOrigin(origins = "*")

public class questionaireController {
     @Autowired
    private JdbcTemplate jdbcTemplate;

    // 更新或插入问卷记录
    @PostMapping("/save/{id}")
    public ResponseEntity<String> saveQuestionnaire(@PathVariable int id, @RequestBody Map<String, Object> formData) {
        
            //System.out.println("Received Request Data: " + request);
            System.out.println("Received asset data: " + formData);//经检验ok

        //     Integer qid;
        // try {
        //     qid = Integer.parseInt((String) formData.get("id"));  // 将 ID 从 String 转换为 Integer
        // } catch (NumberFormatException e) {
        //     return ResponseEntity.badRequest().body("Invalid assetID: Must be an integer.");
        // }
        // System.out.println("qid: " + qid);

            String Q1 = (String) formData.get("Q1");

            String Q2 = (String) formData.get("Q2");
            String Q3 = (String) formData.get("Q3");
            String Q4 = (String) formData.get("Q4");
            String Q5 = (String) formData.get("Q5");
            String Q6 = (String) formData.get("Q6");
            String Q7 = (String) formData.get("Q7");
            String Q8 = (String) formData.get("Q8");
            String Q9 = (String) formData.get("Q9");
            String Q10 = (String) formData.get("Q10");
            String Q11 = (String) formData.get("Q11");
            String Q12 = (String) formData.get("Q12");
            String Q13 = (String) formData.get("Q13");
            String Q14 = (String) formData.get("Q14");
            String Q15 = (String) formData.get("Q15");
            String Q16 = (String) formData.get("Q16");
            String Q17 = (String) formData.get("Q17");
            String Q18 = (String) formData.get("Q18");
            String Q19 = (String) formData.get("Q19");
            String Q20 = (String) formData.get("Q20");
            String Q21 = (String) formData.get("Q21");
            String Q22 = (String) formData.get("Q22");
            String Q23 = (String) formData.get("Q23");
            String Q24 = (String) formData.get("Q24");
            String Q25 = (String) formData.get("Q25");
            String risks = (String) formData.get("risks");
            String warning = (String) formData.get("warning");



        // // 查找是否已有记录
         String checkSql = "SELECT COUNT(*) FROM assets_questionaire WHERE id = ?";
        int count = jdbcTemplate.queryForObject(checkSql, new Object[]{id}, Integer.class);


        if (count > 0) {
            // 如果 Qid 存在，进行更新
            String updateQuery = "UPDATE assets_questionaire SET " +
                    "Q1 = ?, Q2 = ?, Q3 = ?, Q4 = ?, Q5 = ?, " +
                    "Q6 = ?, Q7 = ?, Q8 = ?, Q9 = ?, Q10 = ?, Q11=?,Q12=?,Q13=?,Q14=?,Q15=?,Q16=?,Q17=?,Q18=?,Q19=?,Q20=?,Q21=?,Q22=?,Q23=?,Q24=?,Q25=?,risks=?,warning=?" +
                    "WHERE id = ?";

            jdbcTemplate.update(updateQuery,Q1,Q2,Q3,Q4,Q5,Q6,Q7,Q8,Q9,Q10,Q11,Q12,Q13,Q14,Q15,Q16,Q17,Q18,Q19,Q20,Q21,Q22,Q23,Q24,Q25,risks,warning,id);

        } else {
            // 如果 Qid 不存在，进行插入
            String insertQuery = "INSERT INTO assets_questionaire (" +
                    "id,Q1, Q2,Q3,Q4,Q5,Q6,Q7,Q8,Q9,Q10,Q11,Q12,Q13,Q14,Q15,Q16,Q17,Q18,Q19,Q20,Q21,Q22,Q23,Q24,Q25,risks,warning) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        

            jdbcTemplate.update(insertQuery,id,Q1,Q2,Q3,Q4,Q5,Q6,Q7,Q8,Q9,Q10,Q11,Q12,Q13,Q14,Q15,Q16,Q17,Q18,Q19,Q20,Q21,Q22,Q23,Q24,Q25,risks,warning);

            //         String insertQuery = "INSERT INTO asset_risk_,agement.assets_basic_info (" +
            //         "asset_id, asset_type, associated_assets, contact, description, importance, " +
            //         "qstatus, status, empty_field, swid, created_at, updated_at,asset_owner) " +
            //         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            // jdbcTemplate.update(insertQuery, assetId, assetType, associatedAssets, contact, description,
            //         importance, qStatus, status, emptyFields, swid, createdAt, updatedAt,assetOwner);
        };
             
            return new ResponseEntity<>("Questionnaire updated successfully", HttpStatus.OK);

       
}





}
