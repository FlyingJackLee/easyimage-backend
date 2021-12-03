package com.lizumin.easyimage.service.intf;

import com.lizumin.easyimage.model.entity.ImageLibrary;
import com.lizumin.easyimage.model.entity.User;

import java.awt.*;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/24 8:02 pm
 * 4
 */
public interface ImageLibraryDao {

    /**
     *
     * create library under a user
     *
     * @param username: who owns this library
     * @param name:  the library name
     * @return com.lizumin.easyimage.model.entity.ImageLibrary TODO
     */
    public ImageLibrary createLibrary(String username,String name);


    /**
     *
     * get library by username and libraryname
     *
     * @param username
     * @param libraryname
     * @return com.lizumin.easyimage.model.entity.ImageLibrary TODO
     */
    public ImageLibrary getLibraryByUsernameAndLibraryname(String username,String libraryname);

}
