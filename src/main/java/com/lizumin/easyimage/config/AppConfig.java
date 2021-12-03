package com.lizumin.easyimage.config;

import com.lizumin.easyimage.filter.ExceptionFilter;
import com.lizumin.easyimage.filter.JwtTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/17 7:10 am
 * 4
 */
@Configuration
public class AppConfig implements WebMvcConfigurer  {

    private Environment env;

    @Bean
    public FilterRegistrationBean<ExceptionFilter> exceptionFilterRegistration(
            @Autowired ExceptionFilter exceptionFilter
    ){
        final FilterRegistrationBean<ExceptionFilter> registrationBean = new FilterRegistrationBean<>(exceptionFilter);
        registrationBean.setOrder(-1);
        return registrationBean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtTokenInterceptor()).addPathPatterns("/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**").addResourceLocations(
                "file://" + this.env.getProperty("image.save.path"));
//        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }
}
