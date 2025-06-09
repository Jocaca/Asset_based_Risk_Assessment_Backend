package com.example.assetbasedriskassessmentplatformbackend.controller;

import com.example.assetbasedriskassessmentplatformbackend.entity.User;
import com.example.assetbasedriskassessmentplatformbackend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
//@CrossOrigin(origins = "http://localhost:8080") // 允许跨域
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired // 注入
    private UserMapper userMapper;

    //查找
    @GetMapping("/asset_user_table/queryWholeTable")
    // http://localhost:9090/asset_user_table/queryWholeTable
    public String queryWholeTable(){
        List<User> list= userMapper.findWholeTable();
        System.out.println(list);
        return "search user";
    }


    @RequestMapping("/asset_user_table/queryByID")
    public String queryByID(int assetUserId){
        User user = userMapper.findById(assetUserId);
        return "search by userid";
    }

    @RequestMapping("/asset_user_table/queryByName")
    public String queryByName(String assetUserName){
        User user = userMapper.findByUsername(assetUserName);
        return "search by username";
    }

    @PostMapping("/asset_user_table/save")
    public String save(User user){
        int i = userMapper.insert(user);
        if (i > 0){
            return "save user information success";
        }else{
            return "save user information fail";
        }
    }

    @RequestMapping("/asset_user_table/update")
    public String update(User user){
        int i = userMapper.update(user);
        if (i > 0){
            return "update user information success";
        }else{
            return "update user information fail";
        }
    }

    @PostMapping("/asset_user_table/delete")
    public String delete(int assetUserId){
        int i = userMapper.delete(assetUserId);
        if (i > 0){
            return "delete user information success";
        }else{
            return "delete user information fail";
        }
    }

}
