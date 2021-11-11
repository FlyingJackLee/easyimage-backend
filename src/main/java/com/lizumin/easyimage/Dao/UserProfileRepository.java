package com.lizumin.easyimage.Dao;

import com.lizumin.easyimage.model.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/11 1:34 am
 * 4
 */
public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {
}
