package com.lizumin.easyimage.controller;

import com.auth0.jwt.JWT;
import com.lizumin.easyimage.Dao.UserRepository;
import com.lizumin.easyimage.annos.RequiredJwtToken;
import com.lizumin.easyimage.config.jwt.JtwToken;
import com.lizumin.easyimage.config.jwt.JtwUtil;
import com.lizumin.easyimage.config.jwt.JwtAuthenticationException;
import com.lizumin.easyimage.constant.enums.JWTSetting;
import com.lizumin.easyimage.model.entity.User;


import com.lizumin.easyimage.service.impl.JwtDaoImpl;
import com.lizumin.easyimage.service.intf.JwtCacheDao;
import com.lizumin.easyimage.service.intf.UserDao;
import com.lizumin.easyimage.utils.RandomUtil;
import com.lizumin.easyimage.utils.RestDataUtil;
import io.swagger.annotations.ApiOperation;
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
@CrossOrigin(origins = "http://127.0.0.1:4200", maxAge = 3600)
@RequestMapping(path = "/api/user")
public class UserInfoController {
    private UserRepository userRepository;
    private UserDao userDao;

    private JwtCacheDao jwtCacheDao;
    
    @Operation(summary = "Create user ",description =
            "Register a new user by username,password amd EMail. " +
                    "Also, locale filed, which is not a required filed(default is EN) , points out the locale of the user")
    @PostMapping("/signup")
    public  @ResponseBody RestData createAccount
            (@RequestBody(required = true) RestData requestData){

        String username = RestDataUtil.getKeyData(requestData,"username");
        String password = RestDataUtil.getKeyData(requestData,"password");
        String email = RestDataUtil.getKeyData(requestData,"email");

        User user =  userDao.createUser(username,password,email,Locale.ENGLISH);

        return user != null ? RestData.success("Sign Up complete"): RestData.fail("Sign Up failed");

    }


    @PostMapping("/login")
    public @ResponseBody RestData login(@RequestBody(required = true) RestData requestData){

        String username = RestDataUtil.getKeyData(requestData,"username");
        String password = RestDataUtil.getKeyData(requestData,"password");

        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username,password);

        Subject currentUser = SecurityUtils.getSubject();

        RestData response =  null;

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

    @PostMapping("/logout")
    @RequiredJwtToken
    public @ResponseBody RestData logout(@RequestBody(required = true) RestData requestData){
        String token = RestDataUtil.getKeyData(requestData,"token");
        String username = RestDataUtil.getKeyData(requestData,"username");

        if ( token == null || username == null || "".equals(token) || "".equals(username) ){
            return RestData.fail("Invalid token.");
        }

        //Prevent malicious logout, check token before logout
        if (JtwUtil.verify(token,this.jwtCacheDao.getSecrectKey(username))){
            this.jwtCacheDao.deleteToken(username);
            return RestData.success("you have logged out");
        }
        else {
            return RestData.fail("Bad token.");
        }
    }


    @PostMapping("/find_by_username")
    public @ResponseBody RestData findByUsername(@RequestBody(required = true) RestData requestData){

        String username = RestDataUtil.getKeyData(requestData,"username");

        User user =  this.userRepository.findUserByUsername(username);

        if (user != null){
            return RestData.success("Username has been used");
        }
        else {
            return RestData.fail("Username is available");
        }
    }


    @GetMapping("/check")
    @RequiredJwtToken
    public @ResponseBody RestData checkToken(@RequestHeader(value = JWTSetting.TOKEN_HEADER,required = true) String token){
        return RestData.success("Right token");
    }

//  @PostMapping("/check_token")
//    public @ResponseBody RestData checkToken(@RequestBody(required = true) RestData requestData){
//        RestData response = null;
//
//        String token = RestDataUtil.getKeyData(requestData,"token");
//        String username = RestDataUtil.getKeyData(requestData,"username");
//
//        if ( token == null || username == null || "".equals(token) || "".equals(username) ){
//            return RestData.fail("Invalid token.");
//        }
//
//        boolean isJwtTokenAvailable = this.jwtCacheDao.isJwtTokenAvailable(username);
//
//        if (isJwtTokenAvailable && this.jwtCacheDao.getSecrectKey(username).equals(token.trim())){
//            RestData.success("Valid Token");
//            response.add("expire_time",300);
//        }
//        else {
//            response = RestData.fail("Invalid token.");
//        }
//
//        return response;
//    }



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
