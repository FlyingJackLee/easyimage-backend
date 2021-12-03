package com.lizumin.easyimage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lizumin.easyimage.controller.RestData;
import org.springframework.test.web.servlet.MvcResult;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/12/3 9:47 AM
 * 4
 */
public class RestTestUntil {
    public static String restGenerate(String[] keys,String[] values) throws Exception{
        RestData requestBody = RestData.success();
        for (int i = 0; i < keys.length; i++) {
            requestBody.add(keys[i],values[i]);
        }

        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(requestBody);

    }

    public static RestData responseToRest(MvcResult mvcResult) throws Exception{
        String responseBody =  mvcResult.getResponse().getContentAsString();
        return new ObjectMapper().readValue(responseBody,RestData.class);
    }

}
