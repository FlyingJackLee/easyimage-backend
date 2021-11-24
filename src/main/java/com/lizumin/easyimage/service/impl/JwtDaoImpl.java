package com.lizumin.easyimage.service.impl;

import com.lizumin.easyimage.config.jwt.JwtAuthenticationException;
import com.lizumin.easyimage.constant.enums.JWTSetting;
import com.lizumin.easyimage.service.intf.JwtCacheDao;
import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/17 2:21 am
 * 4
 */
@Service
public class JwtDaoImpl implements JwtCacheDao {

    private StringRedisTemplate stringRedisTemplate;

    private boolean isTokenExisting(String username){
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(username));
    }

    @Override
    public boolean isJwtTokenAvailable(String username) {
        //1.make sure the token is existing
        if (isTokenExisting(username)){
            Long duration = stringRedisTemplate.getExpire(username, TimeUnit.SECONDS);
            //2.make sure the token is not expired
            if (duration > 0){
                    if (duration < 120){
                       refreshToken(username);
                    }
                    return true;
            }
            else {
                deleteToken(username);
                throw new JwtAuthenticationException("Token is expired.");
            }
        }
        else {
            throw new JwtAuthenticationException("Token not exist, please login.");
        }
    }


    @Override
    public void deleteToken(String username) {
        if (isTokenExisting(username)){
           stringRedisTemplate.delete(username);
       }
    }

    @Override
    public void refreshToken(String username) {
        if (isTokenExisting(username)){
            stringRedisTemplate.expire(username,JWTSetting.JWT_DURATION,JWTSetting.JWT_UNIT);
        }
    }

    @Override
    public String getSecrectKey(String username) {
        if (isTokenExisting(username)){
           return stringRedisTemplate.opsForValue().get(username);
        }
        return null;
    }


    @Override
    public void storeJwt(String username, String token) {
        stringRedisTemplate.opsForValue().set(username, token, JWTSetting.JWT_DURATION,JWTSetting.JWT_UNIT);
    }

//    @Override
//    public long tokenExpireTime(String username) {
//        if (isTokenExisting(username)){
//            long expire_time = this.stringRedisTemplate.opsForValue().getOperations().getExpire(username);
//            if (expire_time < 0){
//                deleteToken(username);
//                return -1;
//            }
//            refreshToken(username);
//            return expire_time;
//
//        }
//        return -1;
//    }
//

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }


}
