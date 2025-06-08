package com.example.assetbasedriskassessmentplatformbackend.dto;

import org.springframework.web.bind.annotation.CrossOrigin;


public class UserLoginDTO {
    private String assetUserName;
    private String assetUserPwd;

    public String getPassword() {
        return assetUserPwd;
    }

    public void setPassword(String assetUserPwd) {
        this.assetUserPwd = assetUserPwd;
    }

    public String getUsername() {
        return assetUserName;
    }

    public void setUsername(String assetUserName) {
        this.assetUserName = assetUserName;
    }

    @Override
    public String toString() {
        return "LoginDTO{assetUserName='***', assetUserPwd='***'}"; // 不打印真实密码
    }

}
