package com.lizumin.easyimage.annos;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/17 6:50 am
 * 4
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredJwtToken {

}
