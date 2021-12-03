package com.lizumin.easyimage.dao;

import com.lizumin.easyimage.Dao.UserRepository;
import com.lizumin.easyimage.model.entity.User;
import com.lizumin.easyimage.service.intf.UserDao;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/12/2 9:07 PM
 * 4
 */

@SpringBootTest
public class UserDataTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserDao userDao;

    @BeforeAll
    static void dbInit(@Autowired UserDao userDao){
        userDao.createUser("test","test123","test@test.com", Locale.ENGLISH);
    }

    @AfterAll
    static void destroy( @Autowired UserRepository repository){
        repository.deleteAll();
    }

    @Test
    @Transactional
    void testCreate(){
        //1. test unique username constraint
        assertThatThrownBy(()->{
            User user = this.userDao.createUser("test","test123","test@test.com", Locale.ENGLISH);
        }).isInstanceOf(DataIntegrityViolationException.class).hasMessageContaining("USERNAME");

        //2. normal test
        User user = this.userDao.createUser("jack","test123","test@test.com", Locale.ENGLISH);
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("jack");
        assertThat(user.getEmail()).isEqualTo("test@test.com");
        //should be an encrypted password
        assertThat(user.getPassword()).isNotEqualTo("test123");
    }

    @Test
    void testFindByUsername(){
        //1. success find test
        User user_success = this.repository.findUserByUsername("test");
        assertThat(user_success.getUsername()).isEqualTo("test");

        //2. not find test
        User user_failed = this.repository.findUserByUsername("jack");
        assertThat(user_failed).isNull();
    }


    @Test
    void testFindById(){
        //1. success find test
        Long id = this.repository.findUserByUsername("test").getId();
        User user_success = this.repository.findUserById(id);
        assertThat(user_success.getUsername()).isEqualTo("test");

        //2. not find test
        User user_failed = this.repository.findUserById(99);
        assertThat(user_failed).isNull();
    }


}
