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
//    @GetMapping("/getAllUser")
//    public ResponseEntity<Map<String, Object>> getAllUser() {
//        return userService.getAllUser();
//    }
}
