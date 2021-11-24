package com.lizumin.easyimage.utils;

import com.lizumin.easyimage.controller.RestData;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/23 3:48 pm
 * 4
 */
public class RestDataUtil {
    public static String getKeyData(RestData restData,String key){
        return restData.getData() != null ?
                (String) restData.getData().getOrDefault(key, "")
                : "";

    }
}
