package com.example.assetbasedriskassessmentplatformbackend.dto;

public class UserLoginDTO {
    private String assetUserName;
    private String assetUserPwd;

    public String getAssetUserName() {
        System.out.println("资产用户名: " + assetUserName);
        return assetUserName;
    }

    public void setAssetUserName(String assetUserName) {
        this.assetUserName = assetUserName;
    }

    public String getAssetUserPwd() {
        System.out.println("资产用户密码: " + assetUserPwd);
        return assetUserPwd;
    }

    public void setAssetUserPwd(String assetUserPwd) {
        this.assetUserPwd = assetUserPwd;
    }

    @Override
    public String toString() {
        return "UserLoginDTO{" +
                "assetUserName='" + assetUserName + '\'' +
                ", assetUserPwd='" + assetUserPwd + '\'' +
                '}';
    }
}
