package com.lizumin.easyimage.controller;

import com.lizumin.easyimage.config.jwt.JwtAuthenticationException;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/17 4:22 am
 * 4
 */
@RestControllerAdvice
public class RestErrorController {

    @ExceptionHandler(ShiroException.class)
    public RestData handle401(ShiroException e) {
        return RestData.fail(e.getMessage());
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    public RestData jwtExceptionHandler(JwtAuthenticationException e){
        return e.getRestData();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestData globalException(HttpServletRequest request, Throwable ex) {
        return RestData.fail(ex.getMessage());
    }


}
