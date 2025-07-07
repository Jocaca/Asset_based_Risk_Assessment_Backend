package com.example.assetbasedriskassessmentplatformbackend.ServiceImpl;

import com.example.assetbasedriskassessmentplatformbackend.Service.AuditProjectHomeService;
import com.example.assetbasedriskassessmentplatformbackend.entity.AuditProject;
import com.example.assetbasedriskassessmentplatformbackend.entity.User;
import com.example.assetbasedriskassessmentplatformbackend.repository.AuditProjectHomeRepository;
import com.example.assetbasedriskassessmentplatformbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuditProjectHomeServiceImpl implements AuditProjectHomeService {

    @Autowired
    private AuditProjectHomeRepository auditProjectHomeRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<Map<String, Object>> getAllProjects(int page, int size) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<AuditProject> projects = auditProjectHomeRepository.findAll();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<Map<String, Object>> formattedProjects = projects.stream()
                    .map(project -> {
                        Map<String, Object> projectMap = new HashMap<>();
                        projectMap.put("id", project.getId());
                        projectMap.put("date", dateFormat.format(project.getCreatedDate()));
                        projectMap.put("name", project.getName());
                        projectMap.put("createdBy", project.getCreated_by().getAssetUserName());
                        projectMap.put("status", project.getStatus() == 0 ? "in-progress" : "finished");
                        //projectMap.put("auditor", project.getAuditor().getAssetUserId());

                        Integer auditorId = Optional.ofNullable(project.getAuditor())
                                .map(User::getAssetUserId)
                                .orElse(null);
                        projectMap.put("auditorId", auditorId);

                        return projectMap;
                    })
                    .collect(Collectors.toList());

            System.out.println("---------------------------");

            response.put("success", true);
            response.put("data", formattedProjects);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取项目数据失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> getFilteredProjects(int page, int size, int status) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<AuditProject> projects = auditProjectHomeRepository.findFilteredProjects(status,
                    PageRequest.of(page, size));

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<Map<String, Object>> formattedProjects = projects.stream()
                    .map(project -> {
                        Map<String, Object> projectMap = new HashMap<>();
                        projectMap.put("id", project.getId());
                        projectMap.put("date", dateFormat.format(project.getCreatedDate()));
                        projectMap.put("name", project.getName());
                        projectMap.put("createdBy", project.getCreated_by().getAssetUserName());
                        projectMap.put("status", project.getStatus() == 0 ? "in-progress" : "finished");
                        //projectMap.put("auditor", project.getAuditor().getAssetUserId());

                        Integer auditorId = Optional.ofNullable(project.getAuditor())
                                .map(User::getAssetUserId)
                                .orElse(null);
                        projectMap.put("auditorId", auditorId);


                        return projectMap;
                    })
                    .collect(Collectors.toList());

            response.put("success", true);
            response.put("data", formattedProjects);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取过滤项目数据失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> getSearchProjects(int page, int size, String searchTerm) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<AuditProject> projects = auditProjectHomeRepository.findSearchProjects(searchTerm,
                    PageRequest.of(page, size));

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<Map<String, Object>> formattedProjects = projects.stream()
                    .map(project -> {
                        Map<String, Object> projectMap = new HashMap<>();
                        projectMap.put("id", project.getId());
                        projectMap.put("date", dateFormat.format(project.getCreatedDate()));
                        projectMap.put("name", project.getName());
                        projectMap.put("createdBy", project.getCreated_by().getAssetUserName());
                        projectMap.put("status", project.getStatus() == 0 ? "in-progress" : "finished");
                        // projectMap.put("auditor", project.getAuditor().getAssetUserId());

                        Integer auditorId = Optional.ofNullable(project.getAuditor())
                                .map(User::getAssetUserId)
                                .orElse(null);
                        projectMap.put("auditorId", auditorId);

                        return projectMap;
                    })
                    .collect(Collectors.toList());

            response.put("success", true);
            response.put("data", formattedProjects);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取搜索项目数据失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> createProject(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String projectName = request.get("projectName");
            String createdBy = request.get("createdBy");
            String auditor = request.get("auditor");

            if (projectName == null || projectName.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "项目名称不能为空");
                return ResponseEntity.badRequest().body(response);
            }

            User user = userRepository.findByAssetUserName(createdBy)
                    .orElseThrow(() -> new RuntimeException("用户未找到: " + createdBy));

            User auditorU = userRepository.findByAssetUserName(auditor)
                    .orElseThrow(() -> new RuntimeException("用户未找到: " + auditor));

            System.out.println("------------auditor:"+auditor);

            System.out.println("ProjectName: " + projectName + " | CreatedBy: " + createdBy);

            AuditProject project = new AuditProject();
            project.setName(projectName);
            project.setCreatedDate(new Date());
            project.setCreated_by(user);
            project.setStatus(0); // 默认为in-progress状态
            project.setAuditor(auditorU);


            auditProjectHomeRepository.save(project);

            response.put("success", true);
            response.put("message", "项目创建成功");
            response.put("projectId", project.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "创建项目失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> projectsCount() {
        Map<String, Object> response = new HashMap<>();
        try {
            long count = auditProjectHomeRepository.count();
            response.put("success", true);
            response.put("count", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取项目数量失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> filterProjectsCount(int status) {
        Map<String, Object> response = new HashMap<>();
        try {
            long count = auditProjectHomeRepository.countWithFilters(status);
            response.put("success", true);
            response.put("count", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取过滤项目数量失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> searchProjectsCount(String searchTerm) {
        Map<String, Object> response = new HashMap<>();
        try {
            long count = auditProjectHomeRepository.countWithSearch(searchTerm);
            response.put("success", true);
            response.put("count", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取搜索项目数量失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}