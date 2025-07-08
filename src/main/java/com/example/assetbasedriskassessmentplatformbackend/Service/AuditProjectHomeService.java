package com.example.assetbasedriskassessmentplatformbackend.Service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AuditProjectHomeService {
    ResponseEntity<Map<String, Object>> getAllProjects(int page, int size, int userId, int userLevel);
    ResponseEntity<Map<String, Object>> getFilteredProjects(int page, int size, int userId, int userLevel, int statusCode);
    ResponseEntity<Map<String, Object>> getSearchProjects(int page, int size, int userId, int userLevel, String searchTerm);
    ResponseEntity<Map<String, Object>> createProject(Map<String, String> request);
    ResponseEntity<Map<String, Object>> projectsCount(int userLevel, int userId);
    ResponseEntity<Map<String, Object>> filterProjectsCount(int statusCode, int userLevel, int userId);
    ResponseEntity<Map<String, Object>> searchProjectsCount(String searchTerm, int userLevel, int userId);
}