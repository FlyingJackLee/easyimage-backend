package com.lizumin.easyimage.controller;

import com.lizumin.easyimage.Dao.UserRepository;
import com.lizumin.easyimage.config.jwt.JwtAuthenticationException;
import com.lizumin.easyimage.constant.enums.JWTSetting;
import com.lizumin.easyimage.model.entity.User;
import com.lizumin.easyimage.service.intf.JwtCacheDao;
import com.lizumin.easyimage.service.intf.UserDao;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static com.lizumin.easyimage.controller.RestTestUntil.*;


/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/12/2 7:41 PM
 * 4
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtCacheDao jwtCacheDao;

    public static String token;


    @BeforeAll
    static void dbInit(
            @Autowired UserDao userDao,
            @Autowired MockMvc mvc
    ) throws Exception {
        userDao.createUser("test","test123","test@test.com", Locale.ENGLISH);
        userDao.createUser("test_has_login","test123","test@test.com", Locale.ENGLISH);

        //log in
        String requestBody = restGenerate(
                new String[]{"username","password"},
                new String[]{"test_has_login","test123"});

        MvcResult mvcResult = mvc.perform(
                        post("/api/user/login")
                                .contentType(MediaType.APPLICATION_JSON).content(requestBody.getBytes(StandardCharsets.UTF_8)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.token").isString())
                .andReturn();

        RestData response = responseToRest(mvcResult);

        UserControllerTest.token = (String) (response.getData().get("token"));

    }

    @AfterAll
    static void destroy( @Autowired UserRepository repository){
        repository.deleteAll();
    }


    @Test
    void testSignUp() throws Exception{
        //1. simple signup

        String requestBody = restGenerate(
                new String[]{"username","password","email"},
                new String[]{"test_signup","test123","test@test.com"});


        this.mvc.perform(post("/api/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.getBytes(StandardCharsets.UTF_8)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Sign Up complete"));

        User user =  this.userRepository.findUserByUsername("test_signup");
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("test_signup");
        assertThat(user.getEmail()).isEqualTo("test@test.com");

        //2. bad signup(same username)
        this.mvc.perform(post("/api/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.getBytes(StandardCharsets.UTF_8)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }




    @Test
    void testLogin() throws Exception {
        //1. simple log in
        String requestBody = restGenerate(
                new String[]{"username","password"},
                new String[]{"test","test123"});

        MvcResult mvcResult = this.mvc.perform(
                post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody.getBytes(StandardCharsets.UTF_8)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.token").isString())
                .andReturn();

        RestData response = responseToRest(mvcResult);

        String token = (String) (response.getData().get("token"));
        assertThat(token.length()).isGreaterThan(6);
        assertThat(this.jwtCacheDao.isJwtTokenAvailable("test")).isEqualTo(true);


        //2. bad username
        String requestBody_bad_username = restGenerate(
                new String[]{"username","password"},
                new String[]{"test_not_exist","test123"});

        this.mvc.perform(
                        post("/api/user/login")
                                .contentType(MediaType.APPLICATION_JSON).content(requestBody_bad_username.getBytes(StandardCharsets.UTF_8)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400));


        //3. bad password
        String requestBody_bad_password = restGenerate(
                new String[]{"username","password"},
                new String[]{"test_not_exist","test0000"});
        this.mvc.perform(
                        post("/api/user/login")
                                .contentType(MediaType.APPLICATION_JSON).content(requestBody_bad_password.getBytes(StandardCharsets.UTF_8)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400));

    }

    @Test
    void testLogOut() throws Exception {
        //1. test logout
        String requestBodyLogout = restGenerate(
                new String[]{"username","token"},
                new String[]{"test_has_login",token});

        this.mvc.perform(post("/api/user/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyLogout.getBytes(StandardCharsets.UTF_8))
                        .header(JWTSetting.TOKEN_HEADER,token)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("You have logged out"));

        assertThatThrownBy(()->{
                    this.jwtCacheDao.isJwtTokenAvailable("test_has_login");
                }
        ).isInstanceOf(JwtAuthenticationException.class).hasMessageContaining("Token not exist, please login");


        //2. bad token
        String requestBodyLogoutBad = restGenerate(
                new String[]{"username","token"},
                new String[]{"test_has_login","bad.token"});

        this.mvc.perform(post("/api/user/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyLogout.getBytes(StandardCharsets.UTF_8))
                        .header(JWTSetting.TOKEN_HEADER,"bad.token")
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid token."));

        //3. empty token
        String requestBodyLogoutEmpty = restGenerate(
                new String[]{"username","token"},
                new String[]{"test_has_login",""});

        this.mvc.perform(post("/api/user/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(JWTSetting.TOKEN_HEADER,"bad.token")
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid token."));

    }

    @Test
    void testFindByUsername() throws Exception {
        //1. exist
        String requestBodyLogout = restGenerate(
                new String[]{"username"},
                new String[]{"test"});

        this.mvc.perform(post("/api/user/find_by_username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyLogout.getBytes(StandardCharsets.UTF_8))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Username has been used"));


        //2. not exist
        String requestBodyLogoutBad = restGenerate(
                new String[]{"username"},
                new String[]{"test_not_exist"});

        this.mvc.perform(post("/api/user/find_by_username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyLogoutBad.getBytes(StandardCharsets.UTF_8))
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Username is available"));

    }

    @Test
    void testCheckToken() throws Exception {
        //1. bad token test
        this.mvc.perform(
                            get("/api/user/check")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(JWTSetting.TOKEN_HEADER,"bad.token"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400));


        //2. good token test
        String requestBody = restGenerate(
                new String[]{"username","password"},
                new String[]{"test","test123"});

        MvcResult mvcResult = this.mvc.perform(
                        post("/api/user/login")
                                .contentType(MediaType.APPLICATION_JSON).content(requestBody.getBytes(StandardCharsets.UTF_8)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.token").isString())
                .andReturn();

        RestData response = responseToRest(mvcResult);
        String token = (String) (response.getData().get("token"));

        this.mvc.perform(
                        get("/api/user/check")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(JWTSetting.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}

