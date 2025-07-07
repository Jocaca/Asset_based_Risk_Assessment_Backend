package com.example.assetbasedriskassessmentplatformbackend.Service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AuditProjectHomeService {
    ResponseEntity<Map<String, Object>> getAllProjects(int page, int size);
    ResponseEntity<Map<String, Object>> getFilteredProjects(int page, int size, int status);
    ResponseEntity<Map<String, Object>> getSearchProjects(int page, int size, String searchTerm);
    ResponseEntity<Map<String, Object>> createProject(Map<String, String> request);
    ResponseEntity<Map<String, Object>> projectsCount();
    ResponseEntity<Map<String, Object>> filterProjectsCount(int status);
    ResponseEntity<Map<String, Object>> searchProjectsCount(String searchTerm);
}