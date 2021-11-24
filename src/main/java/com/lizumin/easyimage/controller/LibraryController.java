package com.lizumin.easyimage.controller;

import com.auth0.jwt.JWT;
import com.lizumin.easyimage.Dao.ImageLibraryRepository;
import com.lizumin.easyimage.Dao.UserRepository;
import com.lizumin.easyimage.config.jwt.JtwUtil;
import com.lizumin.easyimage.constant.enums.JWTSetting;
import com.lizumin.easyimage.model.entity.ImageLibrary;
import com.lizumin.easyimage.model.entity.User;
import com.lizumin.easyimage.service.intf.ImageLibraryDao;
import com.lizumin.easyimage.utils.RestDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/24 7:36 pm
 * 4
 */
@RestController
@CrossOrigin(value = "http://127.0.0.1:4200", maxAge = 3600)
@RequestMapping(path = "/api/library")
public class LibraryController {

    private ImageLibraryRepository imageLibraryRepository;
    private UserRepository userRepository;
    private ImageLibraryDao imageLibraryDao;

    @GetMapping("/all")
    public @ResponseBody RestData listAllLibrary
            (@RequestHeader(value = JWTSetting.TOKEN_HEADER,required = true) String token){
        String username = JtwUtil.getUsername(token);

        RestData restData = RestData.success("fetch success");

        List<ImageLibrary> imageLibraries = this.imageLibraryRepository.findAllByUser_Username(username);

        imageLibraries.forEach(
                imageLibrary -> {
                    restData.add(imageLibrary.getId(),imageLibrary.getLibrary_name());
                }
        );

        return restData;
    }


    @GetMapping("/create")
    public @ResponseBody RestData createLibrary
            (
                    @RequestHeader(value = JWTSetting.TOKEN_HEADER,required = true) String token,
                    @RequestBody(required = true) RestData requestData ){
        String libraryName = RestDataUtil.getKeyData(requestData,"name");
        String username = JtwUtil.getUsername(token);
        User user = this.userRepository.findUserByUsername(username);

        ImageLibrary imageLibrary = this.imageLibraryDao.createLibrary(user,libraryName);
        RestData res = RestData.success("success");
        res.add(imageLibrary.getId(),imageLibrary.getLibrary_name());
        return res;
    }

    @Autowired
    public void setImageLibraryRepository(ImageLibraryRepository imageLibraryRepository) {
        this.imageLibraryRepository = imageLibraryRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setImageLibraryDao(ImageLibraryDao imageLibraryDao) {
        this.imageLibraryDao = imageLibraryDao;
    }
}
