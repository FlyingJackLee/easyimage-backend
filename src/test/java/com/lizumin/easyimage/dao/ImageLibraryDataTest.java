package com.lizumin.easyimage.dao;

import com.lizumin.easyimage.Dao.ImageLibraryRepository;
import com.lizumin.easyimage.Dao.ImageRepository;
import com.lizumin.easyimage.Dao.UserRepository;
import com.lizumin.easyimage.model.entity.ImageLibrary;
import com.lizumin.easyimage.model.entity.User;
import com.lizumin.easyimage.service.intf.ImageLibraryDao;
import com.lizumin.easyimage.service.intf.UserDao;
import org.hibernate.TransientObjectException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/12/2 10:02 PM
 * 4
 */
@SpringBootTest
public class ImageLibraryDataTest {
    @Autowired
    private ImageLibraryRepository repository;

    @Autowired
    private ImageLibraryDao imageLibraryDao;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    static void dbInit(@Autowired UserDao userDao, @Autowired ImageLibraryDao imageLibraryDao){
        userDao.createUser("test","test123","test@test.com", Locale.ENGLISH);
        imageLibraryDao.createLibrary("test","test_library");
    }

    @AfterAll
    static void dbDestroy(
            @Autowired UserRepository userRepository,
            @Autowired ImageLibraryRepository libraryRepository){
        libraryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void testCreate(){
        //1. success test(same user, new library name)
        ImageLibrary imageLibrary_1 = this.imageLibraryDao.createLibrary("test","example_1");
        assertThat(imageLibrary_1).isNotNull();
        assertThat(imageLibrary_1.getName()).isEqualTo("example_1");


        //2. bad user
        assertThatThrownBy(()->{
            this.imageLibraryDao.createLibrary("test_not_exist","test_library");
        }).isInstanceOf(RuntimeException.class).hasMessageContaining("User Does not exist");

        //3. unique test(same user, same library name)
        //TODO - bug detect
//        assertThatThrownBy(()->{
//            this.imageLibraryDao.createLibrary("test","test_library");
//        }).isInstanceOf(DataIntegrityViolationException.class).hasMessageContaining("library_name");

    }

    @Test
    void testFindByUsernameAndLibraryName(){
        //1. success find
        ImageLibrary imageLibrary_success = this.imageLibraryDao
                .getLibraryByUsernameAndLibraryname("test","test_library");
        assertThat(imageLibrary_success).isNotNull();
        assertThat(imageLibrary_success.getName()).isEqualTo("test_library");
        assertThat(imageLibrary_success.getUser().getUsername()).isEqualTo("test");

        //2. not find(bad username, good library name)
        assertThatThrownBy(()->{
           this.imageLibraryDao
                    .getLibraryByUsernameAndLibraryname("test_1","test_library");
        }).isInstanceOf(RuntimeException.class).hasMessageContaining("User Does not exist");

        //3. not find(good username, bad library name)
        ImageLibrary imageLibrary_fail_2 = this.imageLibraryDao
                .getLibraryByUsernameAndLibraryname("test","test_library_1");
        assertThat(imageLibrary_fail_2).isNull();
    }

    @Test
    void testFindImageLibraryByNameAndUser() {
        User user = this.userRepository.findUserByUsername("test");

        //1. success find
        ImageLibrary imageLibrary_success = this.repository.findImageLibraryByNameAndUser("test_library",user);
        assertThat(imageLibrary_success).isNotNull();
        assertThat(imageLibrary_success.getName()).isEqualTo("test_library");
        assertThat(imageLibrary_success.getUser().getUsername()).isEqualTo("test");

        //2. not find(bad, good library name)
        assertThatThrownBy(()->{
            this.repository
                    .findImageLibraryByNameAndUser("test_1",new User());
        }).isInstanceOf(InvalidDataAccessApiUsageException.class).hasMessageContaining("object references an unsaved transient instance");

        //3. not find(good username, bad library name)
        ImageLibrary imageLibrary_fail_2 = this.repository
                .findImageLibraryByNameAndUser("test_not_exist",user);
        assertThat(imageLibrary_fail_2).isNull();
    }


    @Test
    void testFindImageLibrariesByUser() {
        List<ImageLibrary> imageLibraries = this.repository.findImageLibrariesByUser(this.userRepository.findUserByUsername("test"));
        assertThat(imageLibraries.size()).isEqualTo(1);
        assertThat(imageLibraries.get(0).getName()).isEqualTo("test_library");
    }



    }
