package com.lizumin.easyimage.controller;

import com.lizumin.easyimage.config.jwt.JwtAuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/24 11:35 pm
 * 4
 */
@ControllerAdvice
public class ErrorController {
    @ExceptionHandler(JwtAuthenticationException.class)
    public RestData jwtExceptionHandler(JwtAuthenticationException e){
        return e.getRestData();
    }
}
