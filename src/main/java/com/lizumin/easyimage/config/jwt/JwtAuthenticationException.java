package com.lizumin.easyimage.config.jwt;

import com.lizumin.easyimage.controller.RestData;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/17 3:04 am
 * 4
 */
public class JwtAuthenticationException extends RuntimeException {
    private RestData restData;

    public JwtAuthenticationException(String msg){
        super(msg);
        this.restData = RestData.fail(msg);
    }
    public JwtAuthenticationException(){
        super();
        this.restData = RestData.fail();
    }

    public RestData getRestData() {
        return restData;
    }
}
