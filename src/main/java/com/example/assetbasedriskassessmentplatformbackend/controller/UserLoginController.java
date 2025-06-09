package com.example.assetbasedriskassessmentplatformbackend.controller;

import com.example.assetbasedriskassessmentplatformbackend.dto.UserLoginDTO;
import com.example.assetbasedriskassessmentplatformbackend.entity.User;
import com.example.assetbasedriskassessmentplatformbackend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class UserLoginController {

    @Autowired
    private UserMapper userMapper;


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody UserLoginDTO loginDTO) {

        //检测后端是否正确接受到参数
        System.out.println("接收到的登录参数: " + loginDTO);

        Map<String, Object> response = new HashMap<>();

        if (userMapper.findByUsername(loginDTO.getAssetUserName()) == null) {
            response.put("success", false);
            response.put("message", "用户名不存在");
            return ResponseEntity.badRequest().body(response);
        }


        User user = userMapper.findByUsername(loginDTO.getAssetUserName());
        System.out.println(user);

        if(!Objects.equals(user.getAssetUserPwd(), loginDTO.getAssetUserPwd())) {
                response.put("success", false);
                response.put("message", "密码错误");
                return ResponseEntity.badRequest().body(response);
            }


        response.put("success", true);
        response.put("message", "登入成功");
        response.put("username", user.getAssetUserName());
        response.put("userId", user.getAssetUserId());
        response.put("useremail", user.getAssetUserEmail());
        return ResponseEntity.ok(response);
    }


}
