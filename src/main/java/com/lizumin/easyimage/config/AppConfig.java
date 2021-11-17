package com.lizumin.easyimage.config;

import com.lizumin.easyimage.filter.JwtTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/17 7:10 am
 * 4
 */
@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtTokenInterceptor()).addPathPatterns("/**");

    }
}
