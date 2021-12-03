package com.lizumin.easyimage.utils;

import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/25 6:27 pm
 * 4
 */
@Component
public class PathUtil {
    public String imagesStorePath(){
          return  imagesStorePath(false);
    }

    public String imagesStorePath(boolean needPrefix){
        String path =  new File("").getAbsolutePath();

        if (needPrefix){
            path = "file://" +  path.substring(0,path.length() - 9) + "easyimage_library/";
        }
        else {
            path = path.substring(0,path.length() - 9) + "easyimage_library/";
        }
        return path;
    }
}
