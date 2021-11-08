package com.lizumin.easyimage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.lizumin.easyimage.config")
public class EasyImageApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyImageApplication.class, args);
    }



}
