package com.example.shoppingmaill.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${uploadPath}")
    String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        // addResourceHandlers 메소드를 오버라이딩하여 파일 업로드 경로 지정
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);
    }
}
