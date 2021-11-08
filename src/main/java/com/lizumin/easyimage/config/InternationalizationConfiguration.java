package com.lizumin.easyimage.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.List;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/4 1:16 pm
 * 4
 */
@Configuration
public class InternationalizationConfiguration {
    private final String[] I18_BASENAMES = new String[]{
            "api"
    };

    @Bean
    public MessageSource messageSource(){
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames(I18_BASENAMES);
        return messageSource;
    }


}
