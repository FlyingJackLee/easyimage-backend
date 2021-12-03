package com.lizumin.easyimage.dao;

import com.lizumin.easyimage.config.jwt.JwtAuthenticationException;
import com.lizumin.easyimage.constant.enums.JWTSetting;
import com.lizumin.easyimage.service.intf.JwtCacheDao;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.*;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/12/2 11:15 PM
 * 4
 */
@SpringBootTest
public class JwtRedisTest {
    @Autowired
    private JwtCacheDao jwtCacheDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @BeforeAll
    static void init(
            @Autowired StringRedisTemplate stringRedisTemplate
    ){
        stringRedisTemplate.opsForValue().set(
                "test", "test_secret_key", JWTSetting.JWT_DURATION,JWTSetting.JWT_UNIT);
    }

    @AfterAll
    static void destroy(
            @Autowired JwtCacheDao jwtCacheDao
    ){
        jwtCacheDao.deleteToken("test");
        jwtCacheDao.deleteToken("test_store");
       }

    @Test
    @Transactional
    void testIsJwtTokenAvailable(){
        assertThat(
                this.jwtCacheDao.isJwtTokenAvailable("test")
        ).isEqualTo(true);

        assertThatThrownBy(()->{
                    this.jwtCacheDao.isJwtTokenAvailable("test_not_exist");

                }
        ).isInstanceOf(JwtAuthenticationException.class).hasMessageContaining("Token not exist, please login");
    }

    @Test
    @Transactional
    void testDeleteToken(){
        stringRedisTemplate.opsForValue().set(
                "test_delete", "test_secret_key", JWTSetting.JWT_DURATION,JWTSetting.JWT_UNIT);
        assertThat(
                this.jwtCacheDao.isJwtTokenAvailable("test_delete")
        ).isEqualTo(true);

        this.jwtCacheDao.deleteToken("test_delete");
        assertThatThrownBy(()->{
                    this.jwtCacheDao.isJwtTokenAvailable("test_delete");
                }
        ).isInstanceOf(JwtAuthenticationException.class).hasMessageContaining("Token not exist, please login");

    }


    @Test
    void testRefreshToken(){
        this.jwtCacheDao.refreshToken("test");
        Long duration = stringRedisTemplate.getExpire("test", TimeUnit.SECONDS);
        assertThat(duration).isGreaterThan(1750);
    }

    @Test
    void testStoreJwt(){
        this.jwtCacheDao.storeJwt("test_store","test_secret_key");
        assertThat(
                this.jwtCacheDao.isJwtTokenAvailable("test_store")
        ).isEqualTo(true);
    }

    @Test
    void testGetSecretKey(){
        assertThat(this.jwtCacheDao.getSecretKey("test")).isEqualTo("test_secret_key");
    }

}
