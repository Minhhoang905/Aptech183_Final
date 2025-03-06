package com.example.Aptech_Final.Security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//Đánh dấu lớp này là một cấu hình Spring
@Configuration
public class WebConfig implements WebMvcConfigurer{
	// Dùng cấu hình WebConfig để hiển thị static files
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**") // Khi truy cập URL bắt đầu bằng "/images/"
                .addResourceLocations("file:src/main/resources/static/images/"); // Chỉ định thư mục vật lý chứa hình ảnh
    }

}
