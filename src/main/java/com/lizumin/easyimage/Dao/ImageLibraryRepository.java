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
     * find all imageLibrary by userprofile id
     *
     * @param id:  userprofile id
     * @return java.util.List<com.lizumin.easyimage.model.entity.ImageLibrary> TODO
     */
    List<ImageLibrary> findImageLibrariesByUserProfileId(long id);



}
