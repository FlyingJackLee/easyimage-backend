package com.lizumin.easyimage.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/16 7:45 pm
 * 4
 */
public class RestData {
    private int code;
    private String message;
    private Map<String,Object> data = new HashMap<>();

    public RestData(){

    }

    public RestData(int code, String message){
        this.code = code;
        this.message = message;
    }

    /**
     *
     * Quick factory method for successful RestData without custom message
     *
     * @return com.lizumin.easyimage.controller.RestData
     */
    public static RestData success(){
        return new RestData(200,"success");
    }

    /**
     *
     * Quick factory method for successful RestData with custom message
     *
     * @param message:  the
     * @return com.lizumin.easyimage.controller.RestData TODO
     */
    public static RestData success(String message){
        return new RestData(200,message);
    }

    /**
     *
     * Quick factory method for failed RestData without custom message
     *
     * @return com.lizumin.easyimage.controller.RestData TODO
     */
    public static RestData fail(){
        return new RestData(400,"failed");
    }

    /**
     *
     * Quick factory method for failed RestData with custom message
     *
     * @param message:  the
     * @return com.lizumin.easyimage.controller.RestData TODO
     */
    public static RestData fail(String message){
        return new RestData(400,message);
    }

    public RestData add(String key,Object value){
        this.data.put(key,value);
        return this;
    }

    public int getCode() {
        return code;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "RestData{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
