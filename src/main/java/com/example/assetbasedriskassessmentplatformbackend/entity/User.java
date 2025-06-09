package com.example.assetbasedriskassessmentplatformbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;


@TableName("asset_user_table")
public class User {
    private int assetUserId;
    private String assetUserName;
    private String assetUserPwd;
    private String assetUserEmail;
    private int assetUserLevel;

    public int getAssetUserId() {
        return assetUserId;
    }

    public void setAssetUserId(int assetUserId) {
        this.assetUserId = assetUserId;
    }

    public String getAssetUserName() {
        return assetUserName;
    }

    public void setAssetUserName(String assetUserName) {
        this.assetUserName = assetUserName;
    }

    public String getAssetUserPwd() {
        return assetUserPwd;
    }

    public void setAssetUserPwd(String assetUserPwd) {
        this.assetUserPwd = assetUserPwd;
    }

    public String getAssetUserEmail() {
        return assetUserEmail;
    }

    public void setAssetUserEmail(String assetUserEmail) {
        this.assetUserEmail = assetUserEmail;
    }

    public int getAssetUserLevel() {
        return assetUserLevel;
    }

    public void setAssetUserLevel(int assetUserLevel) {
        this.assetUserLevel = assetUserLevel;
    }

    @Override
    public String toString() {
        return "User{" +
                "assetUserId=" + assetUserId +
                ", assetUserName='" + assetUserName + '\'' +
                ", assetUserPwd='" + assetUserPwd + '\'' +
                ", assetUserEmail='" + assetUserEmail + '\'' +
                ", assetUserLevel=" + assetUserLevel +
                '}';
    }


}

