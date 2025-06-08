package com.example.assetbasedriskassessmentplatformbackend.controller;

import com.example.assetbasedriskassessmentplatformbackend.dto.UserLoginDTO;
import com.example.assetbasedriskassessmentplatformbackend.entity.User;
import com.example.assetbasedriskassessmentplatformbackend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserLoginController {

    @Autowired
    private UserMapper userMapper;


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody UserLoginDTO loginDTO) {

        Map<String, Object> response = new HashMap<>();


        if (userMapper.findByUsername(loginDTO.getUsername()) == null) {
            response.put("success", false);
            response.put("message", "用户名不存在");
            return ResponseEntity.badRequest().body(response);
        }

        User user = userMapper.findByUsername(loginDTO.getUsername()) ;
        user.setAssetUserName(loginDTO.getUsername());
        user.setAssetUserPwd(loginDTO.getPassword());

        if (!loginDTO.getPassword().equals(user.getAssetUserPwd())) {
            response.put("success", false);
            response.put("message", "密码错误");
            return ResponseEntity.badRequest().body(response);
        }


        response.put("success", true);
        response.put("message", "登录成功");
        response.put("username", user.getAssetUserName());
        response.put("userId", user.getAssetUserId());
        response.put("useremail", user.getAssetUserEmail());

        // 5. 打印调试信息
        System.out.println("登录用户: " + user.getAssetUserName());

        return ResponseEntity.ok(response);

    }

}