package com.example.assetbasedriskassessmentplatformbackend.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface QuestionnaireService {
    ResponseEntity<Map<String, Object>> loadSoftware(int id);
    ResponseEntity<Map<String, Object>> submitSoftware(Integer id,Integer status, Map<String,Object> answer);
}
