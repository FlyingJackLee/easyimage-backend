package com.lizumin.easyimage.mapper;

import com.lizumin.easyimage.model.entity.ImageLibrary;
import com.lizumin.easyimage.model.entity.LabelImage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/4 12:36 pm
 * 4
 */
public interface ImageLibraryRepository extends CrudRepository<ImageLibrary,Integer> {
    List<ImageLibrary> findAllByAccount_Userid(long uuid);

}
