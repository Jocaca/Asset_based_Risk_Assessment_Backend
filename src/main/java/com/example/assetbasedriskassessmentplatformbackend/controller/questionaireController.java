
package com.example.assetbasedriskassessmentplatformbackend.controller;

import com.example.assetbasedriskassessmentplatformbackend.Service.QuestionnaireService;
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
@RequestMapping("/questionnaire")
@CrossOrigin(origins = "*")

public class questionaireController {
    @Autowired
    QuestionnaireService questionnaireService;

    @GetMapping("/load")
    public ResponseEntity<Map<String, Object>> load(@RequestParam int id,
                                                    @RequestParam String type) {
        System.out.println("load");
        switch (type) {
            case "Software":
                return questionnaireService.loadSoftware(id);
            case "Physical":
                return questionnaireService.loadPhysical(id);
            case "Physical_non":
                return questionnaireService.loadPhysicalNon(id);
            case "Database":
                return questionnaireService.loadDatabase(id);
            case "Document":
                return questionnaireService.loadDocument(id);
            case "Patent":
                return questionnaireService.loadPatent(id);
            case "People":
                return questionnaireService.loadPeople(id);
            default:
                return null;
        }
    }

    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitQuestionnaire(
            @RequestBody Map<String, Object> requestBody // 统一接收 JSON
    ) {
        // 1. 提取顶层字段
        Integer id = Integer.parseInt(requestBody.get("id").toString());
        String type = (String) requestBody.get("type");
        Integer status = (Integer) requestBody.get("status");

        // 2. 提取嵌套的 answer 对象
        Map<String, Object> answer = (Map<String, Object>) requestBody.get("answer");
        System.out.println("submit");
        switch (type) {
            case "Software":
                return questionnaireService.submitSoftware(id,status,answer);
            case "Physical":
                return questionnaireService.submitPhysical(id,status,answer);
            case "Physical_non":
                return questionnaireService.submitPhysicalNon(id,status,answer);
            case "Database":
                return questionnaireService.submitDatabase(id,status,answer);
            case "Document":
                return questionnaireService.submitDocument(id,status,answer);
            case "Patent":
                return questionnaireService.submitPatent(id,status,answer);
            case "People":
                return questionnaireService.submitPeople(id,status,answer);
            default:
                return null;
        }
    }
}
