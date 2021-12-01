package com.lizumin.easyimage.service.impl;

import com.lizumin.easyimage.Dao.ImageLibraryRepository;
import com.lizumin.easyimage.Dao.UserRepository;
import com.lizumin.easyimage.model.entity.ImageLibrary;
import com.lizumin.easyimage.model.entity.User;
import com.lizumin.easyimage.service.intf.ImageLibraryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/24 8:04 pm
 * 4
 */
@Service
public class ImageLibraryDaoImpl implements ImageLibraryDao {

    private ImageLibraryRepository imageLibraryRepository;
    private UserRepository userRepository;

    @Override
    public ImageLibrary createLibrary(String username, String name) {
        User user = this.userRepository.findUserByUsername(username);
        if (user == null){
            throw new RuntimeException("User Does not exist");
        }
        ImageLibrary imageLibrary = new ImageLibrary();
        imageLibrary.setName(name);
        imageLibrary.setUser(user);

        this.imageLibraryRepository.save(imageLibrary);

        return imageLibrary;
    }

    @Override
    public ImageLibrary getLibraryByUsernameAndLibraryname(String username, String libraryname) {
        User user = this.userRepository.findUserByUsername(username);
        if (user == null){
            throw new RuntimeException("User Does not exist");
        }
        return this.imageLibraryRepository.findImageLibraryByNameAndUser(libraryname,user);
    }

    @Autowired
    public void setImageLibraryRepository(ImageLibraryRepository imageLibraryRepository) {
        this.imageLibraryRepository = imageLibraryRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
