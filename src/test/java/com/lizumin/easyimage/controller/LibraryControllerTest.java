package com.lizumin.easyimage.controller;

import com.lizumin.easyimage.Dao.ImageLibraryRepository;
import com.lizumin.easyimage.Dao.ImageRepository;
import com.lizumin.easyimage.Dao.ImageTagRepository;
import com.lizumin.easyimage.Dao.UserRepository;
import com.lizumin.easyimage.constant.enums.ImageState;
import com.lizumin.easyimage.constant.enums.JWTSetting;
import com.lizumin.easyimage.model.entity.ImageLibrary;
import com.lizumin.easyimage.model.entity.LabelImage;
import com.lizumin.easyimage.model.entity.Tag;
import com.lizumin.easyimage.service.intf.ImageLibraryDao;
import com.lizumin.easyimage.service.intf.UserDao;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static com.lizumin.easyimage.controller.RestTestUntil.*;
import static org.assertj.core.api.Assertions.*;

/**
 * 2 * @Author: Zumin Li
 * 3 * @Date: 2021/12/3 9:41 AM
 * 4
 */
@SpringBootTest
@AutoConfigureMockMvc
public class LibraryControllerTest {
    @Autowired
    private MockMvc mvc;

    public static String token;
    public static long test_image_id;

    @BeforeAll
    static void dbInit(
                        @Autowired ImageTagRepository tagRepository,
                        @Autowired UserDao userDao,
                       @Autowired ImageLibraryDao imageLibraryDao,
                       @Autowired ImageRepository imageRepository,
                       @Autowired MockMvc mvc) throws Exception {
        userDao.createUser("test","test123","test@test.com", Locale.ENGLISH);
        userDao.createUser("user_empty_library","test123","test@test.com", Locale.ENGLISH);

        ImageLibrary imageLibrary = imageLibraryDao.createLibrary("test","test_library_1");
        imageLibraryDao.createLibrary("test","test_library_2");

        LabelImage image = new LabelImage();
        image.setFilePath("test.png");
        image.setName("test_image");
        image.setState(ImageState.WAITING);
        image.setUploadDate(new Date());
        image.setImageLibrary(imageLibrary);

        test_image_id = imageRepository.save(image).getId();
        LabelImage imageSaved = imageRepository.findLabelImagesById(test_image_id);

        Tag tag = new Tag();
        tag.setType("test_tag");
        tagRepository.save(tag);

        imageSaved.getTags().add(tag);
        imageRepository.save(imageSaved);

        //log in
        String requestBody = restGenerate(
                new String[]{"username","password"},
                new String[]{"test","test123"});

        MvcResult mvcResult = mvc.perform(
                        post("/api/user/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody.getBytes(StandardCharsets.UTF_8)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.token").isString())
                .andReturn();

        RestData response = responseToRest(mvcResult);

        token = (String) (response.getData().get("token"));



    }

    @AfterAll
    static void dbDestroy(
            @Autowired ImageTagRepository tagRepository,
            @Autowired UserRepository userRepository,
            @Autowired ImageRepository imageLibraryDao,
            @Autowired ImageLibraryRepository libraryRepository
    ){
        tagRepository.deleteAll();
        imageLibraryDao.deleteAll();
        libraryRepository.deleteAll();
        userRepository.deleteAll();

        clearImageFiles();
    }


    @Test
    void testListAllLibrary() throws Exception {
        MvcResult mvcResult = this.mvc.perform(
                        get("/api/library/all")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(JWTSetting.TOKEN_HEADER,token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andReturn();

        RestData response = responseToRest(mvcResult);
        assertThat(response.getData().get("test_library_1")).isNotNull().isEqualTo("test.png");
        assertThat(response.getData().get("test_library_2")).isNotNull().isEqualTo("default.jpg");
    }


    @Test
    void tesCreateLibrary() throws Exception {

        //1.exist test
        String requestBodyRepeat = restGenerate(
                new String[]{"username","library_name"},
                new String[]{"test","test_library_1"});

        this.mvc.perform(
                        post("/api/library/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(JWTSetting.TOKEN_HEADER,token)
                                .content(requestBodyRepeat.getBytes(StandardCharsets.UTF_8)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Library Exist!"));


        //2.blank library name
        String requestBodyBlank = restGenerate(
                new String[]{"username","library_name"},
                new String[]{"test",""});

        this.mvc.perform(
                        post("/api/library/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(JWTSetting.TOKEN_HEADER,token)
                                .content(requestBodyBlank.getBytes(StandardCharsets.UTF_8)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Blank library name!"));


        //3.new library name
        String requestBodyNew = restGenerate(
                new String[]{"username","library_name"},
                new String[]{"test","new_library"});

        this.mvc.perform(
                        post("/api/library/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(JWTSetting.TOKEN_HEADER,token)
                                .content(requestBodyNew.getBytes(StandardCharsets.UTF_8)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));


    }

    @Test
    void testListImages() throws Exception {
        MvcResult mvcResult = this.mvc.perform(
                        get("/api/library/images")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(JWTSetting.TOKEN_HEADER,token)
                                .param("library_name","test_library_1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Fetch images successfully")).andReturn();

        RestData response = responseToRest(mvcResult);
        assertThat(response.getData().containsKey(String.valueOf(test_image_id))).isTrue();

    }

    @Test
    void testAddTag() throws Exception {
        //1. normal test
        String requestBody = restGenerate(
                new String[]{"image_id","tag"},
                new String[]{String.valueOf(test_image_id),"test_tag_new"});

        this.mvc.perform(
                        post("/api/library/tag")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(JWTSetting.TOKEN_HEADER,token).content(requestBody.getBytes(StandardCharsets.UTF_8))
                    )
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("test_tag_new add successfully")).andReturn();


        //2. image not find test
        String requestBodyImageNotFound = restGenerate(
                new String[]{"image_id","tag"},
                new String[]{"99999999","test_tag"});

        this.mvc.perform(
                        post("/api/library/tag")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(JWTSetting.TOKEN_HEADER,token)
                                .content(requestBodyImageNotFound.getBytes(StandardCharsets.UTF_8))
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("image not found"));


        //3. Tag Exist test
        String requestBodyTagExist = restGenerate(
                new String[]{"image_id","tag"},
                new String[]{String.valueOf(test_image_id),"test_tag"});

        this.mvc.perform(
                        post("/api/library/tag")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(JWTSetting.TOKEN_HEADER,token)
                                .content(requestBodyTagExist.getBytes(StandardCharsets.UTF_8))
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Tag exist"));
    }

    @Test
    void testUploadImage() throws Exception {
        int countBefore = imageCountInLocalDirectory();

        File test_image = new File("/Users/jack/OneDrive/UofGlasgow_Files/project/code/test.jpg");
        FileInputStream in = new FileInputStream(test_image);

        MultipartFile imageFile = new MockMultipartFile(
            "test.jpg",in.readAllBytes()
        );

        this.mvc.perform(
                MockMvcRequestBuilders.multipart("/api/library/upload")
                        .file("file",imageFile.getBytes())
                        .characterEncoding("UTF-8")
                        .header(JWTSetting.TOKEN_HEADER,token)
                        .param("library_name","test_library_1")

        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Save Success"));

        int countAfter = imageCountInLocalDirectory();
        assertThat(countAfter).isEqualTo(countBefore+1);

    }

    private int imageCountInLocalDirectory(){
        String path = "/Users/jack/OneDrive/UofGlasgow_Files/project/code/easyimage_library";
        int count = 0;
        File d = new File(path);
        File[] list = d.listFiles();
        for (int i = 0; i < list.length;i++) {
            if(list[i].isFile()){
                count++;
            }
        }
        return count;
    }

    private static void clearImageFiles(){
        String path = "/Users/jack/OneDrive/UofGlasgow_Files/project/code/easyimage_library";
        int count = 0;
        File d = new File(path);
        File[] list = d.listFiles();
        for (int i = 0; i < list.length;i++) {
            if(list[i].isFile() && !list[i].getName().equals("default.jpg")){
                list[i].delete();
            }
        }

    }

}
