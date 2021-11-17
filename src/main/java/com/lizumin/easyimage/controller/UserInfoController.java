package com.lizumin.easyimage.controller;

import com.auth0.jwt.JWT;
import com.lizumin.easyimage.Dao.UserRepository;
import com.lizumin.easyimage.annos.RequiredJwtToken;
import com.lizumin.easyimage.config.jwt.JtwToken;
import com.lizumin.easyimage.config.jwt.JtwUtil;
import com.lizumin.easyimage.model.entity.User;


import com.lizumin.easyimage.service.impl.JwtDaoImpl;
import com.lizumin.easyimage.service.intf.JwtCacheDao;
import com.lizumin.easyimage.service.intf.UserDao;
import com.lizumin.easyimage.utils.RandomUtil;
import io.swagger.v3.oas.annotations.Operation;
import net.bytebuddy.utility.RandomString;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.crypto.KeyGenerator;
import java.util.Locale;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/4 1:18 am
 * 4
 */
@RestController
@RequestMapping(path = "/api/user")
public class UserInfoController {
    private UserRepository userRepository;
    private UserDao userDao;

    private JwtCacheDao jwtCacheDao;
    
    @Operation(summary = "Create user ",description =
            "Register a new user by username,password amd EMail. " +
                    "Also, locale filed, which is not a required filed(default is EN) , points out the locale of the user")
    @PostMapping("/register")
    public @ResponseBody User createAccount
            (@RequestParam(name = "username",required = true) String username,
             @RequestParam(name = "password",required = true) String password,
             @RequestParam(name = "email",required = true) String email,
             @RequestParam(name = "locale",required = false)Locale locale
             ){

        return userDao.createUser(username,password,email,locale);
    }


    @PostMapping("/login")
    public @ResponseBody RestData login(@RequestParam(name = "username",required = true) String username,
                                        @RequestParam(name = "password",required = true) String password){
        RestData response =  null;

        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username,password);

        Subject currentUser = SecurityUtils.getSubject();

        try {
            currentUser.login(usernamePasswordToken);
        } catch ( UnknownAccountException | IncorrectCredentialsException uae ) {
            return RestData.fail(uae.getMessage());
        }
        if (currentUser.isAuthenticated()){
            response = RestData.success();

            //crypt the token with random string
            String key = RandomUtil.get10RandomString();
            String token = JtwUtil.sign(key,username);

            //store the secret key into cache
            jwtCacheDao.storeJwt(username,key);

            response.add("token",token);

        }

        return response;
    }

    @GetMapping("/hello")
    @RequiredJwtToken
    public @ResponseBody RestData hello() {
        return RestData.success("hello");
    }


//    @GetMapping("/hello")
//    public @ResponseBody RestData hello(@RequestHeader("Authorization") String token){
//
//        JtwToken jtwToken = new JtwToken(token);
//        Subject currentUser = SecurityUtils.getSubject();
//
//        currentUser.login(jtwToken);
//
//        if (currentUser.isAuthenticated()){
//            return RestData.success("hello");
//        }
//        else {
//            return RestData.fail("hello");
//        }



//
//    @GetMapping("/{id}/profile")
//    public UserProfile findByEmail(@RequestParam(value = "value") String email){
//       return accountRepository.findAccountByEmail(email);
//    }
//    @GetMapping("/find/id")
//    public UserProfile findById(@RequestParam(value = "value") long id){
//        return accountRepository.findAccountByUserid(id);
//    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setJwtCacheDao(JwtCacheDao jwtCacheDao) {
        this.jwtCacheDao = jwtCacheDao;
    }
}
