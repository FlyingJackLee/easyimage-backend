package com.lizumin.easyimage.controller;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/17 3:04 am
 * 4
 */
public class RestfulException extends RuntimeException {
    private RestData restData;

    public RestfulException(String msg){
        super(msg);
        this.restData = RestData.fail(msg);
    }
    public RestfulException(){
        super();
        this.restData = RestData.fail();
    }

    public RestData getRestData() {
        return restData;
    }
}
