package com.example.assetbasedriskassessmentplatformbackend.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:8091")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE","OPTIONS")
                .maxAge(168000)
                .allowedHeaders("*");
//                .allowCredentials(true);

    }
}
