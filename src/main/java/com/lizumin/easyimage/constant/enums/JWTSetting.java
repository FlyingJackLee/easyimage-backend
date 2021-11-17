package com.lizumin.easyimage.constant.enums;

import java.util.concurrent.TimeUnit;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/17 6:35 am
 * 4
 */
public class JWTSetting {
   public final static long JWT_DURATION = 10 * 60; //5 mins
   public final static TimeUnit JWT_UNIT = TimeUnit.SECONDS;
   public final static String TOKEN_HEADER = "Authorization";
}
