package com.example.assetbasedriskassessmentplatformbackend.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface UserService {
    ResponseEntity<Map<String, Object>> login(String assetUserName, String assetUserPwd);
    ResponseEntity<Map<String, Object>> register(String assetUserName,String assetUserEmail, String assetUserPwd);
    ResponseEntity<Map<String, Object>> search(String query);
}
