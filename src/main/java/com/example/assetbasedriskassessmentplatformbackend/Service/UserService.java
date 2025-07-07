package com.example.assetbasedriskassessmentplatformbackend.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface UserService {
    ResponseEntity<Map<String, Object>> login(String assetUserName, String assetUserPwd);
    ResponseEntity<Map<String, Object>> register(String assetUserName,String assetUserEmail, String assetUserPwd);
    ResponseEntity<Map<String, Object>> search(String query);
    ResponseEntity<Map<String, Object>> searchAuditor(String query);
    ResponseEntity<Map<String, Object>> getAllUsers(int page, int size);
    ResponseEntity<Map<String, Object>> usersCount();
    ResponseEntity<Map<String, Object>> filterCount(int permission);
    ResponseEntity<Map<String, Object>> filteredUsers(int page, int size, int permission);
    ResponseEntity<Map<String, Object>> searchCount(String searchTerm);
    ResponseEntity<Map<String, Object>> searchUsers(int page, int size, String searchTerm);
    ResponseEntity<Map<String, Object>> updatePermission(int userId, int permission);
    ResponseEntity<Map<String, Object>> deleteUser(int userId);


}
