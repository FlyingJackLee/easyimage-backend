package com.lizumin.easyimage.service.intf;

import com.lizumin.easyimage.model.entity.User;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Locale;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/11 1:27 am
 * 4
 */
public interface UserDao {
    /**
     *
     * Create a user, and the profile relating to it;
     *
     * @param username: username, required
     * @param password: password, encoded by PasswordEncoder bean, required
     * @param email: the email of a user, required
     * @param locale what language the user prefer, not required
     * @return User: the user created
     */
    User createUser(String username, String password, String email, Locale locale);
}
