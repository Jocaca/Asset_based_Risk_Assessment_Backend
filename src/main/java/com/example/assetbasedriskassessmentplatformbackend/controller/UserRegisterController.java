package com.example.assetbasedriskassessmentplatformbackend.controller;

import com.example.assetbasedriskassessmentplatformbackend.dto.UserRegisterDTO;
import com.example.assetbasedriskassessmentplatformbackend.entity.User;
import com.example.assetbasedriskassessmentplatformbackend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//@CrossOrigin(origins = "*") // 允许跨域
@RestController
@RequestMapping("/api")
public class UserRegisterController {

    @Autowired
    private UserMapper userMapper;


    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @RequestBody UserRegisterDTO registerDTO) {

        Map<String, Object> response = new HashMap<>();

        if (userMapper.findByUsername(registerDTO.getAssetUserName()) != null) {
            response.put("success", false);
            response.put("message", "用户名已存在");
            return ResponseEntity.badRequest().body(response);
        }


        User user = new User();
        user.setAssetUserName(registerDTO.getAssetUserName());
        user.setAssetUserEmail(registerDTO.getAssetUserEmail());
        user.setAssetUserPwd(registerDTO.getAssetUserPwd());
        System.out.println(user);

        if (userMapper.findByUseremail(registerDTO.getAssetUserEmail()) != null) {
            response.put("success", false);
            response.put("message", "邮箱已存在");
            return ResponseEntity.badRequest().body(response);
        }

        int result = userMapper.insert(user);
        if (result <= 0) {
            response.put("success", false);
            response.put("message", "注册失败");
            return ResponseEntity.internalServerError().body(response);
        }

        response.put("success", true);
        response.put("message", "注册成功");
        response.put("username", user.getAssetUserName());
        return ResponseEntity.ok(response);
    }


    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Boolean>> checkUsername(
            @RequestParam String username) {
        boolean exists = userMapper.findByUsername(username) != null;
        return ResponseEntity.ok(Collections.singletonMap("exists", exists));
    }
}
