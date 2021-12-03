package com.lizumin.easyimage.dao;

import com.lizumin.easyimage.Dao.ImageLibraryRepository;
import com.lizumin.easyimage.Dao.ImageRepository;
import com.lizumin.easyimage.Dao.UserRepository;
import com.lizumin.easyimage.constant.enums.ImageState;
import com.lizumin.easyimage.model.entity.ImageLibrary;
import com.lizumin.easyimage.model.entity.LabelImage;
import com.lizumin.easyimage.model.entity.User;
import com.lizumin.easyimage.service.intf.ImageLibraryDao;
import com.lizumin.easyimage.service.intf.UserDao;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/12/2 10:54 PM
 * 4
 */
@SpringBootTest
public class ImageDataTest {

    @Autowired
    private ImageLibraryRepository imageLibraryRepository;

    @Autowired
    private ImageLibraryDao imageLibraryDao;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository repository;

    @BeforeAll
    static void dbInit(@Autowired UserDao userDao,
                       @Autowired ImageLibraryDao imageLibraryDao,
                       @Autowired ImageRepository imageRepository){
        userDao.createUser("test","test123","test@test.com", Locale.ENGLISH);
        ImageLibrary imageLibrary = imageLibraryDao.createLibrary("test","test_library");
        imageLibraryDao.createLibrary("test","empty_library");

        LabelImage image = new LabelImage();
        image.setFilePath("test.png");
        image.setName("test_image");
        image.setState(ImageState.WAITING);
        image.setUploadDate(new Date());
        image.setImageLibrary(imageLibrary);
        imageRepository.save(image);
    }


    @AfterAll
    static void dbDestroy(
            @Autowired UserRepository userRepository,
            @Autowired ImageRepository imageLibraryDao,
            @Autowired ImageLibraryRepository libraryRepository
            ){
        imageLibraryDao.deleteAll();
        libraryRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    void testFindFirstByImageLibrary(){
        //1. find successfully
        LabelImage image = this.repository.findFirstByImageLibrary(this.imageLibraryDao.getLibraryByUsernameAndLibraryname("test","test_library"));
        assertThat(image).isNotNull();
        assertThat(image.getImageLibrary().getName()).isEqualTo("test_library");
        assertThat(image.getName()).isEqualTo("test_image");
        assertThat(image.getState()).isEqualTo(ImageState.WAITING);

        //2. not find
        LabelImage image_fail = this.repository.findFirstByImageLibrary(this.imageLibraryDao.getLibraryByUsernameAndLibraryname("test","empty_library"));
        assertThat(image_fail).isNull();
    }



    @Test
    void testFindLabelImagesByImageLibrary_Name(){
        //1. find successfully
        List<LabelImage> images = this.repository.findLabelImagesByImageLibrary_Name("test_library");
        assertThat(images.size()).isEqualTo(1);
        assertThat(images.get(0).getImageLibrary().getName()).isEqualTo("test_library");
        assertThat(images.get(0).getName()).isEqualTo("test_image");
        assertThat(images.get(0).getState()).isEqualTo(ImageState.WAITING);

        //2. not find
        List<LabelImage> images_empty = this.repository.findLabelImagesByImageLibrary_Name("not_exist_library");
        assertThat(images_empty.size()).isEqualTo(0);
    }

    @Test
    void testFindLabelImagesById(){
        Long id = this.repository.findFirstByImageLibrary(this.imageLibraryDao.getLibraryByUsernameAndLibraryname("test","test_library")).getId();

        LabelImage image = this.repository.findLabelImagesById(id );
        assertThat(image).isNotNull();
        assertThat(image.getImageLibrary().getName()).isEqualTo("test_library");
        assertThat(image.getName()).isEqualTo("test_image");
        assertThat(image.getState()).isEqualTo(ImageState.WAITING);

    }

    @Test
    void testFindLabelImagesByState(){
        //1. waiting
        List<LabelImage> images = this.repository.findLabelImagesByState(ImageState.WAITING);
        assertThat(images.size()).isEqualTo(1);
        assertThat(images.get(0)).isNotNull();
        assertThat(images.get(0).getImageLibrary().getName()).isEqualTo("test_library");
        assertThat(images.get(0).getName()).isEqualTo("test_image");
        assertThat(images.get(0).getState()).isEqualTo(ImageState.WAITING);

        //2. DONE
        List<LabelImage> images_2 = this.repository.findLabelImagesByState(ImageState.DONE);
        assertThat(images_2.size()).isEqualTo(0);

        //3. PROCESSING
        List<LabelImage> images_3 = this.repository.findLabelImagesByState(ImageState.PROCESSING);
        assertThat(images_3.size()).isEqualTo(0);

    }

}
