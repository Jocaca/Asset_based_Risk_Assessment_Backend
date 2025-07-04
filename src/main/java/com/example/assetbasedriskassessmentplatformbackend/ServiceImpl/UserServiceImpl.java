package com.example.assetbasedriskassessmentplatformbackend.ServiceImpl;

import com.example.assetbasedriskassessmentplatformbackend.Service.UserService;
import com.example.assetbasedriskassessmentplatformbackend.entity.User;
import com.example.assetbasedriskassessmentplatformbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<Map<String, Object>> login(String assetUserName, String assetUserPwd) {
        System.out.println("接收到的登录参数: " + assetUserName + ", " + assetUserPwd);

        Map<String, Object> response = new HashMap<>();
        Optional<User> userOptional = userRepository.findByAssetUserName(assetUserName);
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println(user);
            if (!Objects.equals(user.getAssetUserPwd(), assetUserPwd)) {
                response.put("success", false);
                response.put("message", "密码错误");
                return ResponseEntity.badRequest().body(response);
            }
            response.put("success", true);
            response.put("message", "登入成功");
            response.put("username", user.getAssetUserName());
            response.put("userId", user.getAssetUserId());
            response.put("useremail", user.getAssetUserEmail());
            response.put("userLevel", user.getAssetUserLevel());
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "用户名不存在");
            return ResponseEntity.badRequest().body(response);
        }
    }
    public ResponseEntity<Map<String, Object>> register(String assetUserName,String assetUserEmail, String assetUserPwd){
        Map<String, Object> response = new HashMap<>();

        if (userRepository.findByAssetUserName(assetUserName).isPresent()) {
            response.put("success", false);
            response.put("message", "用户名已存在");
            return ResponseEntity.badRequest().body(response);
        }


        User user = new User();
        user.setAssetUserName(assetUserName);
        user.setAssetUserEmail(assetUserEmail);
        user.setAssetUserPwd(assetUserPwd);
        user.setAssetUserLevel(2);
        System.out.println(user);

        if (userRepository.findByAssetUserEmail(assetUserEmail).isPresent()) {
            response.put("success", false);
            response.put("message", "邮箱已存在");
            return ResponseEntity.badRequest().body(response);
        }
        try{
            userRepository.save(user);
            response.put("success", true);
            response.put("message", "注册成功");
            response.put("username", user.getAssetUserName());
            return ResponseEntity.ok(response);
        }catch (Exception e){
            response.put("success", false);
            response.put("message", "注册失败");
            return ResponseEntity.internalServerError().body(response);
        }
    }
    public ResponseEntity<Map<String, Object>> search(String query){
        Map<String, Object> response = new HashMap<>();
        List<User> users = userRepository.findTop5ByInitials(query);

        List<Map<String, Object>> userList = new ArrayList<>();
        for (User user : users) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getAssetUserId());
            userMap.put("name", user.getAssetUserName()); // 假设name存储在username字段
            userList.add(userMap);
        }

        response.put("users", userList);
        response.put("success", true);
//        response.put("count", userList.size());
        return ResponseEntity.ok(response);
    }
}
