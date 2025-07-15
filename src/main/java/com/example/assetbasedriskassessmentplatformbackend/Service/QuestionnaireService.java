package com.example.assetbasedriskassessmentplatformbackend.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface QuestionnaireService {
    ResponseEntity<Map<String, Object>> loadSoftware(int id);
    ResponseEntity<Map<String, Object>> loadPeople(int id);
    ResponseEntity<Map<String, Object>> loadPhysical(int id);
    ResponseEntity<Map<String, Object>> loadPhysicalNon(int id);
    ResponseEntity<Map<String, Object>> loadDatabase(int id);
    ResponseEntity<Map<String, Object>> loadDocument(int id);
    ResponseEntity<Map<String, Object>> loadPatent(int id);
    ResponseEntity<Map<String, Object>> submitSoftware(Integer id,Integer status, Map<String,Object> answer);
    ResponseEntity<Map<String, Object>> submitPeople(Integer id, Integer status, Map<String,Object> answer);
    ResponseEntity<Map<String, Object>> submitPhysical(Integer id,Integer status, Map<String,Object> answer);
    ResponseEntity<Map<String, Object>> submitPhysicalNon(Integer id, Integer status, Map<String,Object> answer);
    ResponseEntity<Map<String, Object>> submitDatabase(Integer id, Integer status, Map<String,Object> answer);
    ResponseEntity<Map<String, Object>> submitDocument(Integer id, Integer status, Map<String,Object> answer);
    ResponseEntity<Map<String, Object>> submitPatent(Integer id, Integer status, Map<String,Object> answer);
}
