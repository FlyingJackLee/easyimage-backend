package com.lizumin.easyimage.utils;

import java.util.Random;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/17 4:08 am
 * 4
 */
public class RandomUtil {

    public static String get10RandomString(){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<10;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
