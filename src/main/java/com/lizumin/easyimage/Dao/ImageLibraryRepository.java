package com.lizumin.easyimage.Dao;

import com.lizumin.easyimage.model.entity.ImageLibrary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/4 12:36 pm
 * 4
 */
public interface ImageLibraryRepository extends JpaRepository<ImageLibrary,Integer> {
    /**
     *
     * ind all imageLibrary by username
     *
     * @param username
     * @return java.util.List<com.lizumin.easyimage.model.entity.ImageLibrary>
     */
    List<ImageLibrary> findAllByUser_Username(String username);

}
