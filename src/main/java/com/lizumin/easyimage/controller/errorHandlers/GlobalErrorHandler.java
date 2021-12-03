package com.lizumin.easyimage.controller.errorHandlers;

import com.lizumin.easyimage.config.jwt.JwtAuthenticationException;
import com.lizumin.easyimage.controller.RestData;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/24 11:35 pm
 * 4
 */
@ControllerAdvice
public class GlobalErrorHandler {
    @ExceptionHandler(JwtAuthenticationException.class)
    public RestData jwtExceptionHandler(JwtAuthenticationException e){
        return e.getRestData();
    }

}
