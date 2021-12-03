package com.lizumin.easyimage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lizumin.easyimage.Dao.ImageLibraryRepository;
import com.lizumin.easyimage.Dao.ImageRepository;
import com.lizumin.easyimage.Dao.ImageTagRepository;
import com.lizumin.easyimage.Dao.UserRepository;
import com.lizumin.easyimage.annos.RequiredJwtToken;
import com.lizumin.easyimage.config.jwt.JtwUtil;
import com.lizumin.easyimage.constant.enums.ImageState;
import com.lizumin.easyimage.constant.enums.JWTSetting;
import com.lizumin.easyimage.model.entity.ImageLibrary;
import com.lizumin.easyimage.model.entity.LabelImage;
import com.lizumin.easyimage.model.entity.Tag;
import com.lizumin.easyimage.model.entity.User;
import com.lizumin.easyimage.service.intf.ImageLibraryDao;
import com.lizumin.easyimage.utils.RandomUtil;
import com.lizumin.easyimage.utils.RestDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/11/24 7:36 pm
 * 4
 */


@RestController
@RequestMapping(path = "/api/library")
public class LibraryController {

    private Environment env;

    private String DEFAULT_COVER =  "default.jpg";

    private ObjectMapper mapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(LibraryController.class);

    private ImageLibraryRepository imageLibraryRepository;
    private UserRepository userRepository;
    private ImageLibraryDao imageLibraryDao;
    private ImageRepository imageRepository;
    private ImageTagRepository tagRepository;

    @GetMapping("/all")
    @RequiredJwtToken
    public @ResponseBody RestData listAllLibrary
            (@RequestHeader(value = JWTSetting.TOKEN_HEADER,required = true) String token){

        String username = JtwUtil.getUsername(token);

        this.logger.debug("Search library username:" + username);

        RestData restData = RestData.success("fetch success");

        User user = this.userRepository.findUserByUsername(username);
        if (user == null){
            return RestData.fail("cannot find this user");
        }

        List<ImageLibrary> imageLibraries = this.imageLibraryRepository.findImageLibrariesByUser(user);

        if (imageLibraries.size() <= 0){
            return RestData.fail("cannot find a library");
        }

        imageLibraries.forEach(
                imageLibrary -> {
                    LabelImage image = this.imageRepository.findFirstByImageLibrary(imageLibrary);
                    String cover = (image != null && !"".equals(image.getFilePath())) ? image.getFilePath() : DEFAULT_COVER;
                    restData.add(imageLibrary.getName(),cover);
                }
        );

        return restData;
    }

    @PostMapping("/create")
    @RequiredJwtToken
    public @ResponseBody RestData createLibrary
            (       @RequestHeader(value = JWTSetting.TOKEN_HEADER,required = true) String token,
                    @RequestBody(required = true) RestData requestData ){
        String libraryName = RestDataUtil.getKeyData(requestData,"library_name");
        String username = JtwUtil.getUsername(token);

        if (libraryName == null || "".equals(libraryName)){
            return RestData.fail("Blank library name!");
        }

        if (this.imageLibraryDao.getLibraryByUsernameAndLibraryname(username,libraryName) != null){
            return RestData.fail("Library Exist!");
        }

        ImageLibrary imageLibrary = this.imageLibraryDao.createLibrary(username,libraryName);

        RestData res = RestData.success("success");
        res.add(String.valueOf(imageLibrary.getId()),imageLibrary.getName());
        return res;
    }

    @PostMapping("/upload")
    @RequiredJwtToken
    public @ResponseBody RestData uploadImage(
            @RequestHeader(value = JWTSetting.TOKEN_HEADER,required = true) String token,
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam("library_name") String library_name

    ){
        if (multipartFile.isEmpty()){
            throw new RestfulException("no file");
        }
        String fileSuffix = Objects.requireNonNull(multipartFile.getOriginalFilename()).contains("jpg") ? ".jpg" : ".png";

        String fileName =  System.currentTimeMillis() + RandomUtil.getRandomString(5) + fileSuffix;
        String abStrPath = this.env.getProperty("image.save.path") + fileName;


        try {
            Path path = Paths.get(abStrPath);

            // Get the file and save it
            byte[] bytes = multipartFile.getBytes();

            Files.write(path, bytes);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RestfulException("Save failed");
        }

        //save the info into db
        LabelImage labelImage = new LabelImage();
        labelImage.setFilePath(fileName);
        labelImage.setState(ImageState.WAITING);
        labelImage.setUploadDate(new Date());

        labelImage.setName(multipartFile.getOriginalFilename().split("\\.")[0]);
        labelImage.setImageLibrary(this.imageLibraryDao.getLibraryByUsernameAndLibraryname(JtwUtil.getUsername(token),library_name));

        this.imageRepository.save(labelImage);

        return RestData.success("Save Success");
    }


    @GetMapping("/images")
    @RequiredJwtToken
    public @ResponseBody RestData listImages(
            @RequestHeader(value = JWTSetting.TOKEN_HEADER,required = true) String token,
            @RequestParam("library_name") String library_name
    ){
        List<LabelImage> labelImages = this.imageRepository.findLabelImagesByImageLibrary_Name(library_name);

        if (labelImages.size()>0){
            RestData restData = RestData.success("Fetch images successfully");
            labelImages.forEach(
                    (LabelImage image) ->{
                        restData.add(String.valueOf(image.getId()),image);
                    }
            );
            return restData;
        }
        else {
            return RestData.success("empty");
        }

    }


    @PostMapping("/tag")
    @RequiredJwtToken
    public @ResponseBody RestData addTag(
            @RequestHeader(value = JWTSetting.TOKEN_HEADER,required = true) String token,
            @RequestBody(required = true) RestData requestData
    ){
        long id = Long.parseLong(RestDataUtil.getKeyData(requestData,"image_id"));
        String tag = RestDataUtil.getKeyData(requestData,"tag");

        if (id <= 0 || tag == null || "".equals(tag)){
            return RestData.fail("image_id or tag wrong");
        }

        LabelImage labelImage = this.imageRepository.findLabelImagesById(id);

        if (labelImage==null){
            return RestData.fail("image not found");
        }

        AtomicBoolean hasExist = new AtomicBoolean(false);
        labelImage.getTags().forEach( (Tag t) ->{
            if (t.getType().equals(tag)){
                hasExist.set(true);
            }
        });

        if (hasExist.get()){
            return RestData.fail("Tag exist");
        }

        Tag newTag = new Tag(tag);
        labelImage.getTags().add(newTag);

        this.tagRepository.save(newTag);
        this.imageRepository.save(labelImage);

        return RestData.success(tag + " add successfully");

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

    @Autowired
    public void setImageRepository(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Autowired
    public void setTagRepository(ImageTagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }
}
