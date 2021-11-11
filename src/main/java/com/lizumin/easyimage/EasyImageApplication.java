package com.lizumin.easyimage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
public class EasyImageApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyImageApplication.class, args);
    }

}
