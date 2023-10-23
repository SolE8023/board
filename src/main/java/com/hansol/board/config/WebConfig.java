package com.hansol.board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.hansol.board.common.Constant.FILE_PATH;
import static com.hansol.board.common.Constant.UPLOAD_FOLDER;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String filePath = "file:///" + FILE_PATH;

        registry.addResourceHandler("/" + UPLOAD_FOLDER + "/**")
                .addResourceLocations(filePath);
    }
}
