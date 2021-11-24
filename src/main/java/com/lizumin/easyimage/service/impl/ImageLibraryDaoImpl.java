package com.lizumin.easyimage.service.impl;

import com.lizumin.easyimage.Dao.ImageLibraryRepository;
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

    @Override
    public ImageLibrary createLibrary(User user, String name) {
        ImageLibrary imageLibrary = new ImageLibrary();
        imageLibrary.setLibrary_name(name);
        imageLibrary.setUser(user);

        this.imageLibraryRepository.save(imageLibrary);

        return imageLibrary;
    }

    @Autowired
    public void setImageLibraryRepository(ImageLibraryRepository imageLibraryRepository) {
        this.imageLibraryRepository = imageLibraryRepository;
    }
}
