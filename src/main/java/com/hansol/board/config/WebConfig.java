package com.hansol.board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.hansol.board.common.Constant.FILE_PATH;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String filePath = "file:///" + FILE_PATH;

        registry.addResourceHandler("/upload/**")
                .addResourceLocations(filePath);
    }
}
