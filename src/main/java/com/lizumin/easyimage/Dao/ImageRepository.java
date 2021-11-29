package com.lizumin.easyimage.Dao;

import com.lizumin.easyimage.constant.enums.ImageState;
import com.lizumin.easyimage.model.entity.ImageLibrary;
import com.lizumin.easyimage.model.entity.LabelImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/25 12:46 am
 * 4
 */
public interface ImageRepository extends JpaRepository<LabelImage,Integer> {

    /**
     *
     * Find first image of specific library
     *
     * @param library
     * @return com.lizumin.easyimage.model.entity.LabelImage
     */
    LabelImage findFirstByImageLibrary(ImageLibrary library);

    /**
     *
     * Find all images in specific library
     *
     * @param library_name
     * @return java.util.List<com.lizumin.easyimage.model.entity.LabelImage>
     */
    List<LabelImage> findLabelImagesByImageLibrary_Name(String library_name);

    /**
     *
     * find image by id
     *
     * @param id
     * @return com.lizumin.easyimage.model.entity.LabelImage
     */
    LabelImage findLabelImagesById(long id);

    /**
     *
     * find images according to its state
     *
     * @param state
     * @return java.util.List<com.lizumin.easyimage.model.entity.LabelImage>
     */
    List<LabelImage> findLabelImagesByState(ImageState state);
}
