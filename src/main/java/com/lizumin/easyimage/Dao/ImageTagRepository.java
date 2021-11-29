package com.lizumin.easyimage.Dao;

import com.lizumin.easyimage.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/25 9:16 pm
 * 4
 */
public interface ImageTagRepository extends JpaRepository<Tag,Integer> {

}
