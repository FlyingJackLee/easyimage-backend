package com.lizumin.easyimage.controller;

import com.lizumin.easyimage.mapper.AccountRepository;
import com.lizumin.easyimage.model.entity.Account;


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
    private AccountRepository accountRepository;

    @PostMapping("/create")
    public @ResponseBody String createAccount
            (@RequestParam(name = "username",required = true) String username,
             @RequestParam(name = "password",required = true) String password,
             @RequestParam(name = "email",required = true) String email
             ){

        Account user = new Account();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);

        System.out.println(user);

        accountRepository.save(user);
        return "Done";
    }

    @GetMapping("/find/email")
    public Account findByEmail(@RequestParam(value = "value") String email){
       return accountRepository.findAccountByEmail(email);
    }
    @GetMapping("/find/id")
    public Account findById(@RequestParam(value = "value") long id){
        return accountRepository.findAccountByUserid(id);
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}
