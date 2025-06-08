package com.example.assetbasedriskassessmentplatformbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.assetbasedriskassessmentplatformbackend.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from asset_user_table")
    public List<User> findWholeTable();

    @Select("select * from asset_user_table where assetUserId=#{assetUserId}")
    public User findById(int assetUserId);


    @Select("SELECT * FROM asset_user_table WHERE assetUserName = #{assetUserName}")
    @Results(id = "userMap", value = {
            @Result(property = "assetUserId", column = "assetUserId"),
            @Result(property = "assetUserName", column = "assetUsername"),
            @Result(property = "assetUserPwd", column = "assetUserPwd")
    })
    public User findByUsername(String assetUserName);

    @Select("SELECT * FROM asset_user_table WHERE assetUserEmail = #{assetUserEmail}")
    public User findByUseremail(String assetUserEmail);

    @Insert("INSERT INTO asset_user_table (assetUsername, assetUserPwd, assetUserEmail) " +
            "VALUES (#{assetUserName}, #{assetUserPwd}, #{assetUserEmail})")
    public int insert(User user);

    @Update("update asset_user_table set assetUserName=#{assetUserName},assetUserPwd=#{assetUserPwd},assetUserEmail=#{assetUserEmail} where assetUserId=#{assetUserId})")
    public int update(User user);

    @Delete("delete from user where assetUserId=#{assetUserId}")
    public int delete(int assetUserId);

}

