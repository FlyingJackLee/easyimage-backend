package com.lizumin.easyimage.controller;

import com.lizumin.easyimage.Dao.UserRepository;
import com.lizumin.easyimage.model.entity.User;
import com.lizumin.easyimage.model.entity.UserProfile;


import com.lizumin.easyimage.service.intf.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/register")
    public @ResponseBody User createAccount
            (@RequestParam(name = "username",required = true) String username,
             @RequestParam(name = "password",required = true) String password
             ){

        return userDao.createUser(username,password);
    }
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
}
