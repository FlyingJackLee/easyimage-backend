package com.lizumin.easyimage.Dao;

import com.lizumin.easyimage.model.entity.ImageLibrary;
import com.lizumin.easyimage.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.awt.*;
import java.util.List;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/4 12:36 pm
 * 4
 */
public interface ImageLibraryRepository extends JpaRepository<ImageLibrary,Integer> {
    /**
     *
     * find all imageLibrary by username
     *
     * @param user
     * @return java.util.List<com.lizumin.easyimage.model.entity.ImageLibrary>
     */
    List<ImageLibrary> findImageLibrariesByUser(User user);

    /**
     *
     * find a imageLibrary by username and libraryname
     *
     * @param libraryname:
     * @return com.lizumin.easyimage.model.entity.ImageLibrary TODO
     */
    ImageLibrary findImageLibraryByNameAndUser(String libraryname,User user);

}
