package com.lizumin.easyimage.service.impl;

import com.lizumin.easyimage.Dao.UserRepository;
import com.lizumin.easyimage.model.entity.User;
import com.lizumin.easyimage.service.intf.UserDao;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Random;

/**
 * This class is provided to AuthenticationConfiguration
 * so the security is able to authentication.
 *
 *
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/11 1:08 am
 * 4
 */
@Service
@Primary
public class UserDaoImpl implements UserDao {
    private UserRepository userRepository;

    private PasswordService passwordService;


    @Override
    public User createUser(String username, String password, String email, Locale locale) {
        User user = new User();
        user.setUsername(username);

        String hashPassword =  passwordService.encryptPassword(password);

        user.setPassword(hashPassword);


        user.setEmail(email);

        if (locale != null){
            user.setLocale(locale);
        }
        else {
            user.setLocale(Locale.ENGLISH);
        }

        return userRepository.save(user);

//        return user;

    }


    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordService(PasswordService passwordService) {
        this.passwordService = passwordService;
    }
}
