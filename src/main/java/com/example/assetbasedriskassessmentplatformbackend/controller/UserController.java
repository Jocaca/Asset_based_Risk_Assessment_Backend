package com.example.assetbasedriskassessmentplatformbackend.controller;

import com.example.assetbasedriskassessmentplatformbackend.Service.UserService;
import com.example.assetbasedriskassessmentplatformbackend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String assetUserName, @RequestParam String assetUserPwd) {
        return userService.login(assetUserName, assetUserPwd);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestParam String assetUserName,@RequestParam String assetUserEmail, @RequestParam String assetUserPwd) {
        return userService.register(assetUserName,assetUserEmail,assetUserPwd);
    }
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> search(@RequestParam String query) {
        return userService.search(query);
    }

    @GetMapping("/search-auditor")
    public ResponseEntity<Map<String, Object>> searchAuditor(@RequestParam String query) {
        System.out.println("search-auditor");
        return userService.searchAuditor(query);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<Map<String, Object>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "15") int size) {
        System.out.println("getAllUsers");
        return userService.getAllUsers(page,size);
    }
    @GetMapping("/users_count")
    public ResponseEntity<Map<String, Object>> usersCount() {
        System.out.println("users_count");
        return userService.usersCount();
    }
    @GetMapping("/filter_count")
    public ResponseEntity<Map<String, Object>> filterCount(@RequestParam int permission) {
        System.out.println("filter_count");
        return userService.filterCount(permission);
    }
    @GetMapping("/filteredUsers")
    public ResponseEntity<Map<String, Object>> filteredUsers(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "15") int size,
                                                             @RequestParam int permission) {
        System.out.println("filteredUsers");
        return userService.filteredUsers(page,size,permission);
    }
    @GetMapping("/search_count")
    public ResponseEntity<Map<String, Object>> searchCount(@RequestParam String searchTerm) {
        System.out.println("search_count");
        return userService.searchCount(searchTerm);
    }
    @GetMapping("/searchUsers")
    public ResponseEntity<Map<String, Object>> searchUsers(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "15") int size,
                                                           @RequestParam String searchTerm) {
        System.out.println("searchUsers");
        return userService.searchUsers(page,size,searchTerm);
    }
    @PostMapping("/updatePermission")
    public ResponseEntity<Map<String, Object>> updatePermission(@RequestParam int userId,
                                                                @RequestParam int permission) {
        System.out.println("updatePermission");
        return userService.updatePermission(userId,permission);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteUser(@RequestParam int userId) {
        System.out.println("delete");
        return userService.deleteUser(userId);
    }

}
