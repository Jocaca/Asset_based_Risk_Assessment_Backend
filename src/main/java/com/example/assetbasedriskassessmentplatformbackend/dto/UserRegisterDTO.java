package com.example.assetbasedriskassessmentplatformbackend.dto;

public class UserRegisterDTO {
    private String assetUserName;
    private String assetUserPwd;
    private String assetUserEmail;

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

    @Override
    public String toString() {
        return "UserRegisterDTO{" +
                "assetUserName='" + assetUserName + '\'' +
                ", assetUserPwd='" + assetUserPwd + '\'' +
                ", assetUserEmail='" + assetUserEmail + '\'' +
                '}';
    }
}
