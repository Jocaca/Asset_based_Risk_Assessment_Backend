package com.example.assetbasedriskassessmentplatformbackend.config;
import com.example.assetbasedriskassessmentplatformbackend.entity.User;
import com.example.assetbasedriskassessmentplatformbackend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class DatabaseInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() == 0) { // 只在数据库为空时插入
                User admin = new User();
                admin.setAssetUserName("admin");
                admin.setAssetUserPwd("admin");
                admin.setAssetUserEmail("admin@example.com");
                admin.setAssetUserLevel(0); // 管理员
                admin.setCreatedAt(new Date());
                admin.setUpdatedAt(new Date());

                userRepository.save(admin);
            }
        };
    }
}