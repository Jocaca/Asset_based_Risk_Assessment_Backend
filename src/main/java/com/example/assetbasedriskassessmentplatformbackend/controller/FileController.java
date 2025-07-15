package com.example.assetbasedriskassessmentplatformbackend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/file")
@CrossOrigin(origins = "*")

public class FileController {

     @Autowired
    private JdbcTemplate jdbcTemplate;

    // 设置文件存储路径
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/static/evidence";  // 当前项目目录下

    // 上传文件处理方法
    @PostMapping("/upload/{rid}")
    public String uploadFile(@PathVariable int rid,@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "No file uploaded";
        }
        //因为treatment_id和rid一一对应，所以保存的是rid

        // 创建文件存储路径
        try {
            // 获取文件原始名称
            String originalFileName = file.getOriginalFilename();
            System.out.println("originalFileName: " + originalFileName);//经检验ok
            // 生成唯一的文件名
            String storedFileName = UUID.randomUUID().toString() + "-" + originalFileName;
            System.out.println("storedFileName: " + storedFileName);//经检验ok
            Path filePath = Paths.get(UPLOAD_DIR, storedFileName);


            // 创建目录，如果不存在
            Files.createDirectories(filePath.getParent());
            

            // 保存文件到指定路径
            //固定路径先
            file.transferTo(filePath.toFile());

            // // 保存文件信息到数据库
            String insertFileSql = "INSERT INTO files (file_path, original_name, stored_name,treatment_id) " +
                     "VALUES (?, ?, ?, ?)";

            // // 执行插入操作
            jdbcTemplate.update(insertFileSql, filePath.toString(), originalFileName, storedFileName,rid);

            return "File uploaded successfully";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error uploading file: " + e.getMessage();  // 返回详细错误信息
        }
    }



     // 获取与传入的 rid 匹配的所有文件
    @GetMapping("/find/{rid}")
    public List<Map<String, Object>> getFilesByTreatmentId(@PathVariable int rid) {
        // 查询 files 表，获取所有 treatment_id 匹配传入 rid 的文件记录
        String query = "SELECT * FROM files WHERE treatment_id = ?";
        try {
            List<Map<String, Object>> files = jdbcTemplate.queryForList(query, rid);
            return files;  // 如果没有找到记录，返回空列表
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 或者返回空的列表，具体取决于需求
        }
    }
    
}
