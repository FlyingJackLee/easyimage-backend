package com.lizumin.easyimage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/11 4:17 am
 * 4
 */
@Configuration
public class SpringFoxConfig {

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build().apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("EasyImage - API Documentation ")
                .version("0.1")
                .description("This is a API documentation for the EasyImage backend restful API.")
                .contact(new Contact("Zumin Li", "", "zumin.li.work@gmail.com"))
                .license("Apache License Version 2.0")
                .build();
    }
}
