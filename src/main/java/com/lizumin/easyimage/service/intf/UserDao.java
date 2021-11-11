package com.lizumin.easyimage.service.intf;

import com.lizumin.easyimage.model.entity.User;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
     * @param username: username
     * @param password: password, encoded by PasswordEncoder bean.
     */
    User createUser(String username, String password);
}
