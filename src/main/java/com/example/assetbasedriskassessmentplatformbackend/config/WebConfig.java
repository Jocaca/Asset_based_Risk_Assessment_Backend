package com.example.assetbasedriskassessmentplatformbackend.config;

import com.example.assetbasedriskassessmentplatformbackend.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    //重写增加拦截器 调用拦截路径
   // @Override
 //   public void addInterceptors(InterceptorRegistry registry){
  //      registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/user/");
 //   }
}
