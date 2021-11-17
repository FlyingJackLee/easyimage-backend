package com.lizumin.easyimage.Dao;

import com.lizumin.easyimage.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/4 12:41 am
 * 4
 */
public interface UserRepository extends JpaRepository<User,Integer> {

    /**
     *
     * find an account in db by username
     *
     * @param username: a string
     * @return com.lizumin.easyimage.model.entity.User
     */
     User findUserByUsername(String username);


     /**
      *
      * find an account in db by id
      *
      * @param id
      * @return com.lizumin.easyimage.model.entity.User TODO
      */
     User findUserById(long id);

}
