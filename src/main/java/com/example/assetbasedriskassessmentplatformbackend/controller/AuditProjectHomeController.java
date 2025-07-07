package com.example.assetbasedriskassessmentplatformbackend.controller;

import com.example.assetbasedriskassessmentplatformbackend.Service.AuditProjectHomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/audit")
@CrossOrigin(origins = "*")
public class AuditProjectHomeController {

    @Autowired
    private AuditProjectHomeService auditProjectHomeService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return auditProjectHomeService.getAllProjects(page, size);
    }

    @GetMapping("/filter")
    public ResponseEntity<Map<String, Object>> getFilteredProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String status) {
        int statusCode = -1;
        if (status != null) {
            switch (status) {
                case "in-progress":
                    statusCode = 0;
                    break;
                case "finished":
                    statusCode = 1;
                    break;
                default:
                    statusCode = -1;
            }
        }
        return auditProjectHomeService.getFilteredProjects(page, size, statusCode);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> getSearchProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam String searchTerm) {
        return auditProjectHomeService.getSearchProjects(page, size, searchTerm);
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createProject(@RequestBody Map<String, String> request) {
        String projectName = request.get("projectName");
        String createdBy = request.get("createdBy");
        return auditProjectHomeService.createProject(request);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> projectsCount() {
        return auditProjectHomeService.projectsCount();
    }

    @GetMapping("/filter-count")
    public ResponseEntity<Map<String, Object>> filterProjectsCount(
            @RequestParam(required = false) String status) {
        int statusCode = -1;
        if (status != null) {
            switch (status) {
                case "in-progress":
                    statusCode = 0;
                    break;
                case "finished":
                    statusCode = 1;
                    break;
                default:
                    statusCode = -1;
            }
        }
        return auditProjectHomeService.filterProjectsCount(statusCode);
    }

    @GetMapping("/search-count")
    public ResponseEntity<Map<String, Object>> searchProjectsCount(
            @RequestParam String searchTerm) {
        return auditProjectHomeService.searchProjectsCount(searchTerm);
    }
}